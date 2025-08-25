create table users (
  id uuid primary key,
  email varchar(255) unique not null,
  password_hash varchar(255) not null,
  created_at timestamp not null default now()
);

create table habits (
  id uuid primary key,
  user_id uuid not null references users(id),
  title varchar(100) not null,
  created_at timestamp not null default now()
);

create table checkins (
  id uuid primary key,
  habit_id uuid not null references habits(id),
  checked_on date not null,
  unique (habit_id, checked_on)
);
