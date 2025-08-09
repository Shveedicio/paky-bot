--liquibase formatted sql

--changeset Shveed:1.0.0.1
create table if not exists image_request_task
(
    id          uuid         not null
        constraint pk_image_request_task primary key,
    telegram_id bigint       not null,
    chat_id     bigint       not null,
    message_id  int          not null,
    image_id    varchar(256) not null,
    payload     text,
    status      varchar(64)  not null
        constraint check_image_request_status
            check (status in ('CREATED',
                              'IMAGE_PROCESSING',
                              'MARKETPLACE_ANALYSIS',
                              'GENERATING_RESPONSE',
                              'SUCCESS',
                              'ERROR')),
    created_at  timestamp    not null,
    updated_at  timestamp    not null
);
