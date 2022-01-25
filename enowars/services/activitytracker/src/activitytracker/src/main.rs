pub mod schema;
pub mod models;
pub mod views;

#[macro_use]
extern crate rocket;
#[macro_use]
extern crate diesel;
extern crate dotenv;
use rocket_dyn_templates::Template;

use std::path::{Path, PathBuf};
use rocket::fs::NamedFile;


use dotenv::dotenv;
use std::env;
use rocket::response::Redirect;
use rocket_auth::User;
use tokio;

use rocket_sync_db_pools::{database};


#[get("/")]
fn index() -> Redirect {
    Redirect::to("/posts")
}

/* Static files Handler */
#[get("/posts/imgs/<file..>")]
async fn assets(file: PathBuf) -> Option<NamedFile> {
    if file.to_str()?.contains('\\') {
        return NamedFile::open(Path::new("imgs/default.jpg")).await.ok();
    }
    if file.to_str()?.contains('/') {
        return NamedFile::open(Path::new("imgs/default.jpg")).await.ok();
    }
    let path = Path::new(env::var("DATA_DIR").unwrap_or("imgs/".to_string()).as_str()).join(file);
    if path.exists() {
        NamedFile::open(path).await.ok()
    } else {
        NamedFile::open(Path::new("imgs/default.jpg")).await.ok()
    }
}

/* Static files Handler for private pictures*/
#[get("/data/imgs/profiles/<file..>")]
async fn assets_private(user: User, file: PathBuf) -> Option<NamedFile> {
    println!("{}", file.to_str()?);
    if !file.to_str()?.starts_with(format!("{}.", user.email()).as_str()) {
        return NamedFile::open(Path::new("imgs/default.jpg")).await.ok();
    }
    if file.to_str()?.contains('\\') {
        return NamedFile::open(Path::new("imgs/default.jpg")).await.ok();
    }
    if file.to_str()?.contains('/') {
        return NamedFile::open(Path::new("imgs/default.jpg")).await.ok();
    }
    let path = Path::new((env::var("DATA_DIR").unwrap_or("imgs/".to_string()) + "profiles/").as_str()).join(file);
    if path.exists() {
        NamedFile::open(path).await.ok()
    } else {
        NamedFile::open(Path::new("imgs/default.jpg")).await.ok()
    }
}


#[database("pg_diesel")]
pub struct PgDieselDbConn(diesel::PgConnection);


#[tokio::main]
async fn main() {
    dotenv().ok();
    let users = rocket_auth::Users::open_postgres(format!("postgres://{}:{}@{}:{}/{}",
                                                          env::var("DB_USER").expect("DB_USER must be set"),
                                                          env::var("DB_PASS").expect("DB_PASS must be set"),
                                                          env::var("DB_HOST").expect("DB_HOST must be set"),
                                                          5432,
                                                          env::var("DB_NAME").expect("DB_NAME must be set"),
    ).as_str()).await.unwrap();


    rocket::build().mount("/", routes![
        index,
        assets,
        assets_private,
        views::posts::get_posts_redirect,
        views::posts::get_posts,
        views::posts::my_posts,
        views::posts::friends_posts,
        views::posts::new,
        views::posts::insert,
        views::posts::update,
        views::posts::process_update,
        views::posts::delete,
        views::posts::delete_old,
        views::auth::get_login,
        views::auth::post_login,
        views::auth::get_signup,
        views::auth::post_signup,
        views::auth::logout,
        views::auth::delete,
        views::auth::get_forgot,
        views::auth::post_forgot,
        views::auth::get_addimage,
        views::auth::post_addimage,
        views::auth::get_viewimages,
        views::friends::new,
        views::friends::insert
    ])
        .manage(users)
        .attach(Template::fairing())
        .attach(PgDieselDbConn::fairing())
        .launch()
    .await.unwrap();
}