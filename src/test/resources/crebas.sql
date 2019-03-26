/*==============================================================*//* DBMS name:      MySQL 5.0                                    *//* Created on:     2019/2/11 21:57:28                           *//*==============================================================*/drop table if exists AUTHORITY;drop table if exists TRANS;drop table if exists SHOP_ORDER;drop table if exists EVALUATE;drop table if exists GOODS;drop table if exists MENU;drop table if exists MER_ADMIN;drop table if exists MESSAGE;drop table if exists RECEIVE_ADDRESS;drop table if exists SHOPPING_CAR;drop table if exists GOODS_MENU;drop table if exists CUSTOM;/*==============================================================*//* Table: AUTHORITY                                             *//*==============================================================*/create table AUTHORITY(   MENU_ID              int comment '????',   AUTH_LEVEL           char(1) comment '???????A0-?Ҧ?????A1-?ק????A2-?d????',   ID                   bigint not null comment '??',   MER_ID               int comment '?޲z???',   primary key (ID));alter table AUTHORITY comment '????t?m??';/*==============================================================*//* Table: CUSTOM                                                *//*==============================================================*/create table CUSTOM(   ID                   bigint not null auto_increment comment '???',   CHANNEL_TYPE         varchar(20) comment '?n???',   NAME                 varchar(20) comment '??',   PHONE 				varchar(11),   OPEN_ID              varchar(40) comment '?ĤT????',   LST_UPD_TIME         timestamp default CURRENT_TIMESTAMP on update  CURRENT_TIMESTAMP comment '?̦Z????',   primary key (ID));alter table CUSTOM comment '????';/*==============================================================*//* Table: EVALUATE                                              *//*==============================================================*/create table EVALUATE(   ID                   varchar(40) not null comment '??',   CREATE_TIME          timestamp default CURRENT_TIMESTAMP comment '????',   GOODS_ID             varchar(40) not null comment '?ӫ~??',   CUS_ID               bigint not null comment '???',   PARENT_ID            varchar(20) comment '????ID',   ORDER_ID             varchar(40) comment '????',   MER_ID               int comment '??ID',   primary key (ID));alter table EVALUATE comment '?ɲ';/*==============================================================*//* Table: GOODS                                                 *//*==============================================================*/create table GOODS(   ID                   varchar(40) not null comment 'ID',   PRICE                decimal(10,2) not null comment '价格',   BRAND                varchar(20) comment '品牌',   DETAIL               blob comment '详情',   INVENTORY            bigint(10) comment '??s',   STATUS               char comment '状态',   IMG_PATH             varchar(200) comment '列表图片',   GOODS_MENU_ID        int,   SPECIFICATION        varchar(200) comment '规格',   TITLE                varchar(25) comment '商品名称',   GOODS_DESC                 varchar(40) comment '商品描述',   MONTH_SALES          bigint(10) comment '月销量',   TOTAL_SALES          bigint(15) comment '总销量',   BANNER_IMAGES        varchar(400) comment '轮播图',   CARRIAGE             int comment '运费',   ORI_PRICE            decimal(10,2) not null comment '原价格',   primary key (ID));alter table GOODS comment '?ӫ~';/*==============================================================*//* Table: MENU                                                  *//*==============================================================*/create table GOODS_MENU(   ID                   int not null auto_increment comment '????',   PARENT_ID            int comment '??????',   MENU_NAME            varchar(20) comment '???W?',   IMAGE_SRC            varchar(128) comment '图片路径',   primary key (ID));/*==============================================================*//* Table: MENU                                                  *//*==============================================================*/create table MENU(   ID                   int not null auto_increment comment '????',   PARENT_ID            int comment '??????',   MENU_NAME            varchar(20) comment '???W?',   primary key (ID));/*==============================================================*//* Table: MER_ADMIN                                             *//*==============================================================*/create table MER_ADMIN(   ID                   int not null comment '??',   NAME                 varchar(20) comment '??',   PASSWORD             varchar(40) comment '?K?',   ACCOUNT_NAME         varchar(20) comment '??',   PHONE_NO             varchar(11) comment '???',   primary key (ID));/*==============================================================*//* Table: MESSAGE                                               *//*==============================================================*/create table MESSAGE(   ID                   varchar(20) not null comment '??',   CONTENT              tinyblob comment '??e',   CREATE_TIME          timestamp comment '????',   CUS_ID               bigint comment '???',   STATUS               char comment '???????A0-????A1-?w?',   primary key (ID));alter table MESSAGE comment '??????';/*==============================================================*//* Table: SHOP_ORDER                                            *//*==============================================================*/create table SHOP_ORDER(   ID                   varchar(40) not null comment '???',   CUS_ID               bigint comment '???',   GOODS_ID             varchar(40) not null comment '?ӫ~ID',   TRANS_NO             varchar(40) comment '???y??',   CREATE_TIME          timestamp default CURRENT_TIMESTAMP,   ORDER_STATUS         char(2) comment '????',   LST_UPD_DATE         timestamp default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP comment '?̦Z????',   DELIVER_STATUS       char(2) comment '????',   ADDRESS_ID           varchar(20) comment '????a?}??',   COUNT                int comment '?ӫ~??q',   TOTAL_ORDR_AMT       decimal(10,2) comment '??????',   DISCOUNT_AMT         decimal(10,2) comment 'ɬ?f???',   PAY_AMT              decimal(10,2) comment '??I???',   primary key (ID));alter table SHOP_ORDER comment '??';/*==============================================================*//* Table: RECEIVE_ADDRESS                                       *//*==============================================================*/create table RECEIVE_ADDRESS(   ID                   varchar(40) not null,   CUS_ID               bigint,   RECV_NAME                 varchar(20) comment '????H?m?W',   PHONE                varchar(11) comment '??t?覡',   PROVICE              varchar(8) comment '??',   CITY                 varchar(8) comment '??',   COUNTY               varchar(8) comment '?',   DETAILED_ADDRESS     varchar(100) comment '???a?}',   CITY_CODE            varchar(10),   LST_UPD_TIME         timestamp default CURRENT_TIMESTAMP on update  CURRENT_TIMESTAMP,   IS_DFT_ADDR          char(1),   primary key (ID));/*==============================================================*//* Table: SHOPPING_CAR                                          *//*==============================================================*/create table SHOPPING_CAR(   ID                   varchar(40) not null comment '?y??',   CUS_ID               bigint comment '???',   CREATE_TIME          timestamp default CURRENT_TIMESTAMP comment '?K?[??????',   CURRENT_PRICE        decimal(10,2) comment '?K?[?????ɲ??',   GOODS_ID             varchar(40) not null comment '?ӫ~??',   COUNT                int comment '?ӫ~??q',   primary key (ID));alter table SHOPPING_CAR comment '????';/*==============================================================*//* Table: TRANS                                                 *//*==============================================================*/create table TRANS(   TRACE_NO             varchar(40) not null comment '???y??',   TRX_STATUS           char(2) not null comment '????,00-???\?A03-?ݤ??A01-?????',   BACK_CHANNEL         char(2) comment '?I?ڳq?D,01-????A02-?L?H',   TRX_AMT              decimal(10,2) comment '?????',   CUS_ID               bigint not null comment '???',   ORI_TRACE_NO         varchar(40) comment '?????y??',   TRX_CODE             char(6) comment '???',   REFUNDED_AMT         decimal(10,2) comment '?w?h???',   REFUNDABLE_AMT       decimal(10,2) comment '?i?h???',   BACK_CHNL_TRACE_NO   varchar(40) comment '?q?D?y??',   primary key (TRACE_NO));alter table TRANS comment '交易表';