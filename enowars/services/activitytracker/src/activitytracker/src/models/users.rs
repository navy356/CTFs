use diesel::pg::PgConnection;
use crate::schema::users;
use crate::diesel::RunQueryDsl;
use crate::models::users::users::dsl as dsl;
use crate::diesel::prelude::*;
use serde::{Serialize};
use std::env;


#[derive(Queryable, Identifiable, Serialize, Clone)]
pub struct User {
    pub id: i32,
    pub email: String,
    pub password: String,
    pub is_admin: bool,
    pub verification_image: String,
    pub post_count: i32,
    pub created: std::time::SystemTime,
}

#[derive(Insertable)]
#[table_name="users"]
pub struct NewUser<'a> {
    pub email: &'a str,
    pub password: &'a str,
    pub is_admin: bool,
    pub verification_image: &'a str,
}

pub fn create_user(conn: &PgConnection, email: &str, password: &str, is_admin: bool, verification_image: &str) -> User {
    let new_user = NewUser {
        email, password, is_admin, verification_image
    };

    diesel::insert_into(users::table)
        .values(&new_user)
        .get_result(conn)
        .expect("Error creating user.")
}

pub async fn delete_old_users(conn: &crate::PgDieselDbConn) {
    use std::fs;
    let timestamp = std::time::SystemTime::now() - std::time::Duration::from_secs(60*10);
    let query = users::table
        .filter(dsl::created.lt(timestamp));
    let user_list: Vec<String> = conn.run(move |c| query
        .select(dsl::email)
        .load::<String>(c)).await.expect("Error loading users")
        .into_iter()
        .collect();
    let paths = fs::read_dir(format!("{}profiles", env::var("DATA_DIR").unwrap_or("/".to_string()))).unwrap();
    for path in paths {
        let p = path.unwrap().path().display().to_string();
        for user in &user_list {
            let prefix = format!("{}profiles/{}", env::var("DATA_DIR").unwrap_or("/".to_string()), user);
            if p.starts_with(prefix.as_str()) {
                fs::remove_file(&p).unwrap();
                break;
            }
        }
    }
    conn.run(move |c| diesel::delete(query).execute(c)).await.expect("Error deleting users");
}

pub async fn get_user_id(conn: &crate::PgDieselDbConn, email: String) -> i32 {
    conn.run(move |c| users::table
        .select(dsl::id)
        .filter(dsl::email.eq(email))
        .first(c)).await
        .expect("No such user.")
}

pub async fn update_user(conn: &crate::PgDieselDbConn, id: i32, password: String) -> User {
    conn.run(move |c| diesel::update(dsl::users.find(id))
        .set(dsl::password.eq(password))
        .get_result(c)).await
        .expect("Error updating user.")
}

pub async fn update_user_image(conn: &crate::PgDieselDbConn, email: String, image: String) -> User {
    conn.run(move |c| diesel::update(dsl::users.filter(dsl::email.eq(email)))
        .set(dsl::verification_image.eq(image))
        .get_result(c)).await
        .expect("Error updating user.")
}