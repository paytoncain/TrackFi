alter table transaction_entity add constraint _transaction_category_fk foreign key (category_id) references category_entity(id);

alter table rule_entity add constraint _rule_category_fk foreign key (category_id) references category_entity(id);