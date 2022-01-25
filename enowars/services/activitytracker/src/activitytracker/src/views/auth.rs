// from https://github.com/tvallotton/rocket_auth/blob/master/examples/postgres.rs, but slightly
// modified!!

use rocket::{*};
use rocket::form::Form;
use rocket_auth::*;
use rocket_dyn_templates::Template;
use serde_json::json;
use rocket::response::{Flash, Redirect};
use rocket::request::FlashMessage;
use crate::models::users::{update_user_image, get_user_id, update_user};
use std::env;
use file_diff::{diff};
use argon2::{self};
use chrono::{Datelike, Utc, Timelike};
use rand::Rng;

use rand::random;
use rocket::fs::TempFile;

pub fn rand_string(size: usize) -> String {
    (0..)
        .map(|_| random::<u8>())
        .filter(|n| 31 < *n && *n < 126)
        .map(char::from)
        .take(size)
        .collect()
}

#[get("/auth/login")]
pub fn get_login(flash: Option<FlashMessage>) -> Template {
    if let Some(ref msg) = flash {
        Template::render("auth/login", json!({
            "flash": msg.message()
        }))
    } else {
        Template::render("auth/login", json!({}))
    }
}

#[post("/auth/login", data = "<form>")]
pub async fn post_login(mut auth: Auth<'_>, form: Form<Login>) -> Flash<Redirect> {
    let res = auth.login(&form).await;
    match res {
        Ok(()) => Flash::success(
            Redirect::to("/posts/view/0"),
            "Logged in!",
        ),
        Err(e) => Flash::error(
            Redirect::to("/auth/login"),
            format!(
                "Error creating user: {}",
                e.to_string()
            )
        )
    }
}

#[get("/auth/signup")]
pub fn get_signup(flash: Option<FlashMessage>) -> Template {
    if let Some(ref msg) = flash {
        Template::render("auth/signup", json!({
            "flash": msg.message()
        }))
    } else {
        Template::render("auth/signup", json!({}))
    }
}


#[derive(Debug, FromForm)]
pub struct SignupForm<'v> {
    email: String,
    password: String,
    image: Option<TempFile<'v>>,
}


#[post("/auth/signup", data = "<post_data>")]
pub async fn post_signup(mut auth: Auth<'_>, conn: crate::PgDieselDbConn, post_data: Form<SignupForm<'_>>) -> Flash<Redirect> {
    let mut pdata = post_data.into_inner();
    let email = pdata.email;
    let password= pdata.password;
    let form: Signup = serde_json::from_str(format!("{{\"email\": \"{}\", \"password\": \"{}\"}}", email, password).as_str()).unwrap();
    match auth.signup(&form).await {
        Err(e) => return Flash::error(
            Redirect::to("/auth/signup"),
            format!(
                "Error creating user: {}",
                e.to_string()
            ),
        ),
        _ => ()
    };

    match pdata.image.as_mut() {
        Some(image) => {
            match image.name() {
                Some (_s) => {let now = Utc::now();
                    let absolute_path: String = format!("{}profiles/{}", env::var("DATA_DIR").unwrap_or("imgs/".to_string()).as_str(), format!("{}.{:02}-{:02}-{:02}.png", email, now.hour(), now.minute(), now.second()));
                    let absolute_path_without_date: String = format!("{}profiles/{}", env::var("DATA_DIR").unwrap_or("imgs/".to_string()).as_str(), format!("{}.", email));
                    image.copy_to(absolute_path).await.unwrap();

                    update_user_image(&conn, email, absolute_path_without_date).await;
                }
                ,
                None => ()
            }
        }
        None => ()
    }

    match auth.login(&form.into()).await {
        Err(e) => return Flash::error(
            Redirect::to("/auth/login"),
            format!(
                "User created but error logging in: {}",
                e.to_string()
            ),
        ),
        _ => ()
    };
    Flash::success(
        Redirect::to("/posts/view/0"),
        "Logged in!",
    )
}


#[get("/auth/viewimages")]
pub fn get_viewimages(user: User, flash: Option<FlashMessage>) -> Template {
    use std::fs;
    let user_image = format!("{}profiles/", env::var("DATA_DIR").unwrap_or("/".to_string())) + user.email();
    let paths = fs::read_dir(format!("{}profiles", env::var("DATA_DIR").unwrap_or("/".to_string()))).unwrap();
    let mut images: Vec<String> = vec![];
    for path in paths {
        let p = path.unwrap().path().display().to_string();
        if p.starts_with(&user_image) {
            images.push(p);
        }
    }

    Template::render("auth/viewimages", json!({
            "user": user.email().to_string(),
            "images": images,
            "flash": match flash {
                Some(ref msg) => msg.message(),
                None => "List of activities"
            },
        }))
}


#[get("/auth/addimage")]
pub fn get_addimage(user: User, flash: Option<FlashMessage>) -> Template {
    if let Some(ref msg) = flash {
        Template::render("auth/addimage", json!({
            "flash": msg.message(),
            "user": user.email().to_string()
        }))
    } else {
        Template::render("auth/addimage", json!({"user": user.email().to_string()}))
    }
}

#[derive(Debug, FromForm)]
pub struct ImageForm<'v> {
    image: TempFile<'v>,
}

#[post("/auth/addimage", data = "<post_data>")]
pub async fn post_addimage(user: User, mut post_data: Form<ImageForm<'_>>) -> Flash<Redirect> {
    let email = user.email();

    let now = Utc::now();

    let absolute_path: String = format!("{}profiles/{}", env::var("DATA_DIR").unwrap_or("imgs/".to_string()).as_str(), format!("{}.{:02}-{:02}-{:02}.png", email, now.hour(), now.minute(), now.second()));
    post_data.image.copy_to(absolute_path).await.unwrap();

    Flash::success(
        Redirect::to("/auth/viewimages"),
        "Added new image!",
    )
}

#[get("/auth/forgot")]
pub fn get_forgot(flash: Option<FlashMessage>) -> Template {
    if let Some(ref msg) = flash {
        Template::render("auth/forgot", json!({
            "flash": msg.message()
        }))
    } else {
        Template::render("auth/forgot", json!({}))
    }
}


#[derive(Debug, FromForm)]
pub struct ForgotForm<'v> {
    email: String,
    password: String,
    image: TempFile<'v>,
}


#[post("/auth/forgot", data = "<post_data>")]
pub async fn post_forgot(mut auth: Auth<'_>, conn: crate::PgDieselDbConn, post_data: Form<ForgotForm<'_>>) -> Flash<Redirect> {
    use std::fs;

    let mut pdata = post_data.into_inner();
    let email = pdata.email;
    let password= pdata.password;
    let user_id = get_user_id(&conn, email.clone()).await;
    pdata.image.persist_to(format!("{}.png", rand::thread_rng().gen_range(0..i32::MAX))).await.unwrap();
    let upload_image = pdata.image.path().unwrap();

    let user_image = format!("{}profiles/{}.", env::var("DATA_DIR").unwrap_or("imgs/".to_string()).as_str(), email);

    let mut matching_image_found = false;
    let paths = fs::read_dir(format!("{}profiles", env::var("DATA_DIR").unwrap_or("/".to_string()))).unwrap();
    for path in paths {
        let p = path.unwrap().path().display().to_string();
        if p.starts_with(&user_image) {
            if diff(upload_image.to_str().unwrap(), p.as_str()) {
                matching_image_found = true;
                break;
            }
        }
    }

    if matching_image_found { // images are the same
        let password_bytes = password.as_bytes();
        let salt = rand_string(10);
        let config = argon2::Config::default();
        let hash = argon2::hash_encoded(password_bytes, &salt.as_bytes(), &config).unwrap();
        update_user(&conn, user_id, hash).await;
        let form: Signup = serde_json::from_str(format!("{{\"email\": \"{}\", \"password\": \"{}\"}}", email.clone(), password).as_str()).unwrap();
        match auth.login(&form.into()).await {
            Err(e) => return Flash::error(
                Redirect::to("/auth/login"),
                format!(
                    "User created but error logging in: {}",
                    e.to_string()
                ),
            ),
            _ => Flash::success(
                Redirect::to("/posts/view/0"),
                "Logged in!",
            )
        }
    } else {
        return Flash::error(
            Redirect::to("/auth/forgot"),
            format!(
                "Verification images do not match"
            )
        )
    }

}

#[get("/auth/logout")]
pub fn logout(mut auth: Auth) -> Result<Redirect, String> {
    auth.logout().unwrap();
    Ok(Redirect::to("/"))
}
#[get("/auth/delete")]
pub async fn delete(mut auth: Auth<'_>) -> Result<Redirect, String> {
    auth.delete().await.expect("Could not delete post!");
    Ok(Redirect::to("/"))
}