table! {
    friends (id) {
        id -> Int4,
        sender_id -> Int4,
        receiver_id -> Int4,
    }
}

table! {
    posts (id) {
        id -> Int4,
        body -> Text,
        deleted -> Bool,
        visibility -> Text,
        image -> Nullable<Text>,
        user_id -> Int4,
        user_post_count -> Int4,
        protected -> Bool,
    }
}

table! {
    users (id) {
        id -> Int4,
        email -> Varchar,
        password -> Varchar,
        is_admin -> Bool,
        verification_image -> Text,
        post_count -> Int4,
        created -> Timestamp,
    }
}

joinable!(posts -> users (user_id));

allow_tables_to_appear_in_same_query!(
    friends,
    posts,
    users,
);
