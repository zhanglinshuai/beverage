-- auto-generated definition



/*用户表的建表语句*/
create table user
(
    id             bigint auto_increment comment '用户id'
        primary key,
    username       varchar(255)                       null comment '用户名',
    password       varchar(255)                       null comment '密码',
    real_name      varchar(255)                       null comment '真实姓名',
    phone          varchar(255)                       null comment '手机号',
    email          varchar(255)                       null comment '邮箱',
    avatar         varchar(255)                       null comment '头像',
    status         tinyint                            null comment '账号状态 0-正常  1-禁用',
    last_login_time datetime default CURRENT_TIMESTAMP not null comment '最后登录时间',
    create_time    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime default CURRENT_TIMESTAMP not null comment '更新时间',
    constraint user_pk_2
        unique (username)
)
comment '所有角色共用一张表，通过角色区分身份';

/* 用户表的建表语句*/
create table role
(
    id          bigint auto_increment comment '角色id'
        primary key,
    role_name   varchar(255)                       null comment '角色名称',
    role_code   varchar(255)                       null comment '角色标识',
    description varchar(255)                       null comment '角色描述',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '角色表';

/*用户角色表的建表语句*/
create table user_role
(
    id          bigint auto_increment comment '用户角色id'
        primary key,
    user_id     bigint                             null comment '用户id',
    role_id     bigint                             null comment '角色id',
    role_code   varchar(255)                       null comment '角色标识',
    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '用户角色表';

/*设备表的建表语句*/
create table device
(
    id           bigint auto_increment comment '设备id'
        primary key,
    device_code  varchar(255)                           null comment '设备编号',
    device_name  varchar(255)                           null comment '设备名称',
    device_model varchar(255)                           null comment '设备型号',
    location     varchar(255)                           null comment '安装位置',
    longitude    decimal(10, 6)                         null comment '经度',
    latitude     decimal(10, 6)                         null comment '纬度',
    status       varchar(255) default 'ONLINE'          not null comment '设备状态',
    operation_id bigint                                 null comment '运营商id',
    install_time datetime     default CURRENT_TIMESTAMP not null comment '安装时间',
    create_time  datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time  datetime     default CURRENT_TIMESTAMP not null comment '更新时间'
)
    comment '设备表';

/*库存表建表语句 */
create table inventory
(
    id            bigint auto_increment comment '库存id'
        primary key,
    device_id     bigint                             null comment '设备id',
    product_id    bigint                             null comment '商品id',
    stock         int      default 0                 not null comment '当前库存',
    warning_stock int                                null comment '库存预警值',
    update_time   datetime default CURRENT_TIMESTAMP not null comment '更新时间'
)
    comment '库存表-存储各个设备的各个商品还有多少库存';

/*商品表的建表语句*/
create table product
(
    id           bigint auto_increment comment '商品id'
        primary key,
    product_name varchar(255)                       null comment '商品名称',
    brand        varchar(255)                       null comment '品牌',
    price        decimal(10, 2)                     null comment '价格',
    image        varchar(255)                       null comment '商品图片',
    description  varchar(255)                       null comment '商品描述',
    create_time  datetime default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '商品表';

/*订单表的建表语句*/
create table orders
(
    id           bigint auto_increment comment '订单id'
        primary key,
    order_no     varchar(100)                       null comment '订单编号',
    device_id    bigint                             null comment '设备id',
    product_id   bigint                             null comment '商品id',
    unit_price   decimal(10, 2)                     null comment '商品单价',
    quantity     int      default 1                 not null comment '购买数量',
    total_price  decimal(10, 2)                     null comment '总金额',
    pay_type     varchar(255)                       null comment '支付方式',
    order_status varchar(255)                       null comment '订单状态',
    create_time  datetime default CURRENT_TIMESTAMP not null comment '创建时间'
)
    comment '订单表';

/*工单表的建表语句*/
create table work_order
(
    id            bigint auto_increment comment '工单id'
        primary key,
    work_no       varchar(255)                       null comment '工单编号',
    device_id     bigint                             null comment '设备id',
    operator_id   bigint                             null comment '运营商id',
    maintainer_id bigint                             null comment '运维人员id',
    work_type     varchar(255)                       null comment '工单类型',
    work_status   varchar(255)                       null comment '工单状态',
    description   varchar(255)                       null comment '工单问题描述',
    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    finish_time   datetime                           null comment '完成时间'
)
    comment '工单表';