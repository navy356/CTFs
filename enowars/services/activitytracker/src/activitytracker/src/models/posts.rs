use crate::schema::posts;
use crate::schema::users;
use crate::diesel::RunQueryDsl;
use crate::models::posts::posts::dsl as dsl;
use crate::diesel::prelude::*;

use serde::{Serialize};
use crate::models::users::User;
use diesel::expression::dsl::count;
use crate::models::friends::get_all_friends;


#[derive(Queryable, Serialize, Associations, Identifiable)]
#[belongs_to(User, foreign_key = "user_id")]
pub struct Post {
    pub id: i32,
    pub body: String,
    pub deleted: bool,
    pub visibility: String,
    pub image: Option<String>,
    pub user_id: i32,
    pub user_post_count: i32,
    pub protected: bool,
}

#[derive(Insertable, AsChangeset)]
#[table_name="posts"]
pub struct NewPost {
    pub body: String,
    pub visibility: String,
    pub image: Option<String>,
    pub user_id: i32,
    pub user_post_count: i32,
    pub protected: bool,
}

#[derive(Serialize)]
pub struct UsersAndPosts(pub User, Vec<Post>);
impl From<(User, Vec<Post>)> for UsersAndPosts {
    fn from(elements: (User, Vec<Post>)) -> Self {
        UsersAndPosts(elements.0, elements.1)
    }
}
impl UsersAndPosts {
    pub async fn load_all(email_id: i32, page_size: usize, page: usize, conn: &crate::PgDieselDbConn) -> Vec<UsersAndPosts>{
        let friends: Vec<i32> = get_all_friends(&conn, email_id).await;
        let users: Vec<User> = conn.run(move |c| users::table
            .filter(users::post_count.gt(0).or(users::id.eq(email_id)).or(users::id.eq_any(&friends)))
            .order_by(users::id.desc())
            .limit(page_size as i64)
            .offset((page*page_size) as i64)
            .load::<User>(c)).await.expect("Error loading users").into_iter().collect();
        let friends: Vec<i32> = get_all_friends(&conn, email_id).await;
        let users_clone = users.clone();
        let posts = conn.run(move |c| Post::belonging_to(&users_clone)
            .filter(posts::deleted.eq(false))
            .filter(posts::visibility.eq("public").or(posts::user_id.eq(email_id)).or(posts::user_id.eq_any(&friends).and(posts::visibility.eq("friends"))))
            .load::<Post>(c)).await.expect("Error loading posts")
            .grouped_by(&users);
        let mut res = users.into_iter().zip(posts).map(UsersAndPosts::from).collect::<Vec<_>>();
        res.retain(|uap| !uap.1.is_empty());
        res
    }
    pub async fn load_mine(email_id: i32, conn: &crate::PgDieselDbConn) -> Vec<UsersAndPosts>{
        let user: Vec<User> = conn.run(move |c| users::table.filter(users::id.eq(email_id)).load::<User>(c)).await.expect("Error loading users").into_iter().collect();
        let user_clone = user.clone();
        let posts = conn.run(move |c| Post::belonging_to(&user_clone)
            .filter(posts::deleted.eq(false))
            .load::<Post>(c)).await.expect("Error loading posts")
            .grouped_by(&user);
        user.into_iter().zip(posts).map(UsersAndPosts::from).collect::<Vec<_>>()
    }
    pub async fn load_friends(email_id: i32, conn: &crate::PgDieselDbConn) -> Vec<UsersAndPosts>{
        let friends: Vec<i32> = get_all_friends(conn, email_id).await;
        let friends_clone = friends.clone();
        let user: Vec<User> = conn.run(move |c| users::table.filter(users::id.eq_any(&friends_clone)).load::<User>(c)).await.expect("Error loading users").into_iter().collect();
        let user_clone = user.clone();
        let posts = conn.run(move |c| Post::belonging_to(&user_clone)
            .filter(posts::deleted.eq(false))
            .filter(posts::visibility.eq("public").or(posts::user_id.eq(email_id)).or(posts::user_id.eq_any(&friends).and(posts::visibility.eq("friends"))))
            .load::<Post>(c)).await.expect("Error loading posts")
            .grouped_by(&user);
        user.into_iter().zip(posts).map(UsersAndPosts::from).collect::<Vec<_>>()
    }
    pub async fn post_count(email_id: i32, conn: &crate::PgDieselDbConn) -> i64 {
        conn.run(move |c| users::table
            .filter(users::post_count.gt(0).or(users::id.eq(email_id)))
            .select(count(users::id))
            .first::<i64>(c)).await.expect("Error getting post count.")
    }
}

pub async fn update_visible_post_count(user_id: i32, conn: &crate::PgDieselDbConn) {
    let visible_post_count = conn.run(move |c| (posts::table
        .filter(posts::user_id.eq(user_id))
        .filter(posts::visibility.eq("public"))
        .filter(posts::deleted.eq(false))
        .select(count(posts::id))
        .first::<i64>(c)).expect("Error updating user.")).await as i32;

    conn.run(move |c| diesel::update(users::table.find(user_id))
        .set(users::post_count.eq(visible_post_count))
        .get_result::<User>(c)).await
        .expect("Error updating user.");
}

pub async fn create_post(conn: crate::PgDieselDbConn, body: String, visibility: String, image: Option<String>, user_id: i32, protected: bool) -> Post {


    let user_post_count = conn.run(move |c| (posts::table
        .select(count(posts::id))
        .filter(posts::user_id.eq(user_id))
        .first::<i64>(c)).expect("Error saving post.") + 1).await as i32; // Trust me, this is safe!

    let res = conn.run(move |c| diesel::insert_into(posts::table)
        .values(NewPost {
            body,
            visibility,
            image,
            user_id,
            user_post_count,
            protected
        })
        .get_result(c)
    ).await.expect("Error saving post.");

    update_visible_post_count(user_id, &conn).await;
    res
}

pub async fn update_post(conn: crate::PgDieselDbConn, id: i32, body: Option<String>, visibility: Option<String>, image: Option<String>) -> Post {
    match body {
        Some(b) => {
            conn.run(move |c| diesel::update(dsl::posts.find(id))
                .set(dsl::body.eq(b))
                .get_result::<Post>(c)).await
                .expect("Error updating post.");
            ()
        },
        None => ()
    };
    match image {
        Some(i) => {
            conn.run(move |c| diesel::update(dsl::posts.find(id))
                .set(dsl::image.eq(i))
                .get_result::<Post>(c)).await
                .expect("Error updating post.");
            ()
        },
        None => ()
    };
    match visibility {
        Some(v) => {
            let post = conn.run(move |c| diesel::update(dsl::posts.find(id))
                .set(dsl::visibility.eq(v))
                .get_result::<Post>(c)).await
                .expect("Error updating post.");
            update_visible_post_count(post.user_id, &conn).await;
            ()
        },
        None => ()
    };
    conn.run(move |c| posts::table.filter(dsl::id.eq(id)).first(c)).await.expect("Error loading post.")

}

pub async fn delete_post(conn: crate::PgDieselDbConn, id: i32) -> usize {
    let post = conn.run(move |c| diesel::update(dsl::posts.find(id))
        .set(dsl::deleted.eq(true))
        .get_result::<Post>(c)).await
        .expect("Error deleting post.");
    update_visible_post_count(post.user_id, &conn).await;
    1
}