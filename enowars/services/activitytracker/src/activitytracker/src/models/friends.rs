use crate::schema::friends;
use crate::diesel::RunQueryDsl;
use crate::diesel::prelude::*;
use serde::{Serialize};
use crate::models::users::User;
use crate::models::friends::friends::dsl as dsl;


#[derive(Queryable, Serialize, Associations, Identifiable)]
#[belongs_to(User, foreign_key = "receiver_id")]
pub struct Friend {
    pub id: i32,
    pub sender_id: i32,
    pub receiver_id: i32,
}

#[derive(Insertable, AsChangeset)]
#[table_name="friends"]
pub struct NewFriend {
    pub sender_id: i32,
    pub receiver_id: i32,
}


pub async fn create_friend(conn: &crate::PgDieselDbConn, sender_id: i32, receiver_id: i32) -> Friend {
    let new_friend = NewFriend {
        sender_id, receiver_id
    };

    conn.run(move |c| diesel::insert_into(friends::table)
        .values(&new_friend)
        .get_result(c)).await
        .expect("Error creating friendship.")
}


pub async fn get_all_friends(conn: &crate::PgDieselDbConn, your_id: i32) -> Vec<i32> {
    conn.run(move |c| friends::table
        .select(dsl::sender_id)
        .filter(dsl::receiver_id.eq(your_id))
        .get_results(c)).await
        .expect("Error getting friends.")
}