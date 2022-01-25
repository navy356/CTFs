use rocket_dyn_templates::Template;

use rocket::request::FlashMessage;
use rocket::response::{Flash, Redirect};
use rocket::form::Form;

use rocket_auth::User;
use serde_json::json;
use crate::models::users::get_user_id;
use crate::models::friends::create_friend;


#[get("/friends")]
pub async fn new(user: User, flash: Option<FlashMessage<'_>>) -> Template {
    let (m_name, m_msg) = match flash {
        Some(ref msg) => (msg.kind(), msg.message()),
        None => ("success", "Add a friend!")
    };
    Template::render("friends/friends_add", json!({
            "flash": if m_name == "success" {m_msg} else {""},
            "user": user.email().to_string(),
            "err": if m_name == "error" {m_msg} else {""}
        }))
}

#[derive(Debug, FromForm)]
pub struct EmailForm {
    email: String
}

#[post("/friends/insert", data = "<post_data>")]
pub async fn insert(user: User, conn: crate::PgDieselDbConn, post_data: Form<EmailForm>) -> Flash<Redirect> {
    let other = get_user_id(&conn, post_data.into_inner().email).await;
    create_friend(&conn, user.id(), other).await;

    Flash::success(
        Redirect::to("/posts/view/0"),
        "Success! You added a friend! They can now see your activity!",
    )
}