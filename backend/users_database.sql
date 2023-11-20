drop table if exists users;

create table users(
	id varchar(10),
	name varchar(5),
	score int,
	primary key (id)
);

create or replace function highscore()
returns trigger
language plpgsql
as $$
begin
	if(	select 1
		from users
		where id = new.id and score >= new.score) then
		return null;
	end if;

	delete from users
	where id = new.id and score < new.score;

	return new;
end;
$$;

create trigger update_highscore before insert on users
for each row
execute function highscore();

insert into users values ('2312','DFA',90);
insert into users values ('36805','UTG',800);
insert into users values ('1234','CUE',100);
insert into users values ('75443','QTK',500);
insert into users values ('583','QYB',-70);
insert into users values ('1049','DHJ',30);
insert into users values ('1680','OPE',9000);
insert into users values ('75443','QTK',200);
insert into users values ('703','TNE',330);
insert into users values ('3780','QTB',330);
insert into users values ('146','KYR',150);
insert into users values ('2312','DFA',1000);

select *
from users;

with ranked_users as (
	select id, name, score, rank() over (order by score desc) as ranking
	from users
)
select id, name, score, ranking
from ranked_users
where ranking <= 5
order by score desc;

