--
-- PostgreSQL database dump
--

-- Dumped from database version 12.2 (Debian 12.2-4)
-- Dumped by pg_dump version 13.3 (Debian 13.3-1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: bank_account; Type: TABLE; Schema: public; Owner: dr_eremin
--

CREATE TABLE public.bank_account (
    id bigint NOT NULL,
    client_id bigint NOT NULL,
    current_balance numeric(20,2) DEFAULT 0.00 NOT NULL,
    CONSTRAINT positive_current_balance CHECK ((current_balance >= (0)::numeric)),
    CONSTRAINT positive_id CHECK ((client_id > 0))
);


ALTER TABLE public.bank_account OWNER TO dr_eremin;

--
-- Name: bank_account_id_seq; Type: SEQUENCE; Schema: public; Owner: dr_eremin
--

CREATE SEQUENCE public.bank_account_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.bank_account_id_seq OWNER TO dr_eremin;

--
-- Name: bank_account_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dr_eremin
--

ALTER SEQUENCE public.bank_account_id_seq OWNED BY public.bank_account.id;


--
-- Name: client; Type: TABLE; Schema: public; Owner: dr_eremin
--

CREATE TABLE public.client (
    id bigint NOT NULL,
    firstname character varying(50) NOT NULL,
    lastname character varying(50) NOT NULL,
    patronymic character varying(50) NOT NULL,
    birthday date NOT NULL,
    address text NOT NULL,
    CONSTRAINT client_firstname_rgx CHECK (((firstname)::text ~ similar_escape('[A-Za-zА-Яа-я]{1,50}'::text, NULL::text))),
    CONSTRAINT client_lastname_rgx CHECK (((firstname)::text ~ similar_escape('[A-Za-zА-Яа-я]{1,50}'::text, NULL::text))),
    CONSTRAINT client_patronymic_rgx CHECK (((firstname)::text ~ similar_escape('[A-Za-zА-Яа-я]{1,50}'::text, NULL::text)))
);


ALTER TABLE public.client OWNER TO dr_eremin;

--
-- Name: client_id_seq; Type: SEQUENCE; Schema: public; Owner: dr_eremin
--

CREATE SEQUENCE public.client_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.client_id_seq OWNER TO dr_eremin;

--
-- Name: client_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dr_eremin
--

ALTER SEQUENCE public.client_id_seq OWNED BY public.client.id;


--
-- Name: email; Type: TABLE; Schema: public; Owner: dr_eremin
--

CREATE TABLE public.email (
    id bigint NOT NULL,
    email_address character varying(100) NOT NULL,
    CONSTRAINT email_address_rgx CHECK (((email_address)::text ~ similar_escape('[A-Za-z]{1}([A-Za-z0-9]|[-_\.])*@([A-Za-z0-9]|[-_\.])+\.[A-Za-z]{2,4}'::text, NULL::text)))
);


ALTER TABLE public.email OWNER TO dr_eremin;

--
-- Name: email_client; Type: TABLE; Schema: public; Owner: dr_eremin
--

CREATE TABLE public.email_client (
    email_id bigint NOT NULL,
    client_id bigint NOT NULL
);


ALTER TABLE public.email_client OWNER TO dr_eremin;

--
-- Name: email_id_seq; Type: SEQUENCE; Schema: public; Owner: dr_eremin
--

CREATE SEQUENCE public.email_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.email_id_seq OWNER TO dr_eremin;

--
-- Name: email_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dr_eremin
--

ALTER SEQUENCE public.email_id_seq OWNED BY public.email.id;


--
-- Name: operation; Type: TABLE; Schema: public; Owner: dr_eremin
--

CREATE TABLE public.operation (
    id bigint NOT NULL,
    account_id bigint NOT NULL,
    operation_type_id integer NOT NULL,
    date_time timestamp with time zone NOT NULL,
    transaction_amount numeric(20,2) NOT NULL,
    CONSTRAINT account_id_positive CHECK ((account_id > 0)),
    CONSTRAINT operation_type_id_positive CHECK ((operation_type_id > 0)),
    CONSTRAINT transaction_amount_positive CHECK ((transaction_amount >= (0)::numeric))
);


ALTER TABLE public.operation OWNER TO dr_eremin;

--
-- Name: operation_id_seq; Type: SEQUENCE; Schema: public; Owner: dr_eremin
--

CREATE SEQUENCE public.operation_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.operation_id_seq OWNER TO dr_eremin;

--
-- Name: operation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dr_eremin
--

ALTER SEQUENCE public.operation_id_seq OWNED BY public.operation.id;


--
-- Name: operation_type; Type: TABLE; Schema: public; Owner: dr_eremin
--

CREATE TABLE public.operation_type (
    id integer NOT NULL,
    operation_name character varying(100) NOT NULL
);


ALTER TABLE public.operation_type OWNER TO dr_eremin;

--
-- Name: operation_type_id_seq; Type: SEQUENCE; Schema: public; Owner: dr_eremin
--

CREATE SEQUENCE public.operation_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.operation_type_id_seq OWNER TO dr_eremin;

--
-- Name: operation_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dr_eremin
--

ALTER SEQUENCE public.operation_type_id_seq OWNED BY public.operation_type.id;


--
-- Name: passport; Type: TABLE; Schema: public; Owner: dr_eremin
--

CREATE TABLE public.passport (
    id bigint NOT NULL,
    client_id bigint NOT NULL,
    series character varying(4) NOT NULL,
    passport_number character varying(6) NOT NULL,
    date_issue date NOT NULL,
    whom_issued_by text NOT NULL,
    code_division character varying(7) NOT NULL,
    CONSTRAINT passport_code_division CHECK (((code_division)::text ~ similar_escape('[0-9]{3}-[0-9]{3}'::text, NULL::text))),
    CONSTRAINT passport_passport_number_rgx CHECK (((passport_number)::text ~ similar_escape('[0-9]{6}'::text, NULL::text))),
    CONSTRAINT passport_series_rgx CHECK (((series)::text ~ similar_escape('[0-9]{4}'::text, NULL::text)))
);


ALTER TABLE public.passport OWNER TO dr_eremin;

--
-- Name: passport_id_seq; Type: SEQUENCE; Schema: public; Owner: dr_eremin
--

CREATE SEQUENCE public.passport_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.passport_id_seq OWNER TO dr_eremin;

--
-- Name: passport_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dr_eremin
--

ALTER SEQUENCE public.passport_id_seq OWNED BY public.passport.id;


--
-- Name: phone; Type: TABLE; Schema: public; Owner: dr_eremin
--

CREATE TABLE public.phone (
    id bigint NOT NULL,
    phone_number character varying(16) NOT NULL,
    CONSTRAINT phone_phone_number_rgx CHECK (((phone_number)::text ~ similar_escape('\+7\([0-9]{3}\)[0-9]{3}-[0-9]{2}-[0-9]{2}'::text, NULL::text)))
);


ALTER TABLE public.phone OWNER TO dr_eremin;

--
-- Name: phone_client; Type: TABLE; Schema: public; Owner: dr_eremin
--

CREATE TABLE public.phone_client (
    phone_id bigint NOT NULL,
    client_id bigint NOT NULL
);


ALTER TABLE public.phone_client OWNER TO dr_eremin;

--
-- Name: phone_id_seq; Type: SEQUENCE; Schema: public; Owner: dr_eremin
--

CREATE SEQUENCE public.phone_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.phone_id_seq OWNER TO dr_eremin;

--
-- Name: phone_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: dr_eremin
--

ALTER SEQUENCE public.phone_id_seq OWNED BY public.phone.id;


--
-- Name: transfer_recipient; Type: TABLE; Schema: public; Owner: dr_eremin
--

CREATE TABLE public.transfer_recipient (
    recipient_account_id bigint NOT NULL,
    operation_id bigint NOT NULL,
    CONSTRAINT transfer_recepient_account_id_check CHECK ((recipient_account_id > 0)),
    CONSTRAINT transfer_recepient_operation_id_check CHECK ((operation_id > 0))
);


ALTER TABLE public.transfer_recipient OWNER TO dr_eremin;

--
-- Name: bank_account id; Type: DEFAULT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.bank_account ALTER COLUMN id SET DEFAULT nextval('public.bank_account_id_seq'::regclass);


--
-- Name: client id; Type: DEFAULT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.client ALTER COLUMN id SET DEFAULT nextval('public.client_id_seq'::regclass);


--
-- Name: email id; Type: DEFAULT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.email ALTER COLUMN id SET DEFAULT nextval('public.email_id_seq'::regclass);


--
-- Name: operation id; Type: DEFAULT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.operation ALTER COLUMN id SET DEFAULT nextval('public.operation_id_seq'::regclass);


--
-- Name: operation_type id; Type: DEFAULT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.operation_type ALTER COLUMN id SET DEFAULT nextval('public.operation_type_id_seq'::regclass);


--
-- Name: passport id; Type: DEFAULT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.passport ALTER COLUMN id SET DEFAULT nextval('public.passport_id_seq'::regclass);


--
-- Name: phone id; Type: DEFAULT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.phone ALTER COLUMN id SET DEFAULT nextval('public.phone_id_seq'::regclass);


--
-- Data for Name: bank_account; Type: TABLE DATA; Schema: public; Owner: dr_eremin
--

COPY public.bank_account (id, client_id, current_balance) FROM stdin;
2	1	101156.00
4	6	100505.88
6	7	59167564.86
9	4	327934.27
7	3	10000.50
1	2	39817499.90
5	5	154850.15
\.


--
-- Data for Name: client; Type: TABLE DATA; Schema: public; Owner: dr_eremin
--

COPY public.client (id, firstname, lastname, patronymic, birthday, address) FROM stdin;
1	Даниил	Михайлов	Маркович	1972-02-12	Автобусная ул., д. 5, кв. 30
2	Дамир	Исаев	Давидович	1950-07-07	Бассейная ул., д. 1, кв. 29
3	Константин	Ковалев	Даниилович	1965-03-08	Варшавская ул., д. 56, кв. 28
4	Михаил	Карпов	Макарович	1967-06-05	Гдовская ул., д. 23, кв. 27
5	Евгений	Щербаков	Игоревич	1971-02-14	Дегтярёва ул., д. 4, кв. 26
6	Ангелина	Степанова	Андреевна	2009-01-27	Егорова ул., д. 6, кв. 25
7	Ксения	Белкина	Тимуровна	1969-01-09	Ждановская наб., д. 67, кв. 24
\.


--
-- Data for Name: email; Type: TABLE DATA; Schema: public; Owner: dr_eremin
--

COPY public.email (id, email_address) FROM stdin;
1	kaft93x@outlook.com
2	dcu@yandex.ru
3	pa5h@mail.ru
4	rv7bp@gmail.com
5	vubx0t@mail.ru
6	ic0pu@outlook.com
7	mek975vcx@gmail.com
\.


--
-- Data for Name: email_client; Type: TABLE DATA; Schema: public; Owner: dr_eremin
--

COPY public.email_client (email_id, client_id) FROM stdin;
1	4
2	1
3	7
4	5
5	2
6	6
7	3
\.


--
-- Data for Name: operation; Type: TABLE DATA; Schema: public; Owner: dr_eremin
--

COPY public.operation (id, account_id, operation_type_id, date_time, transaction_amount) FROM stdin;
1	2	1	2021-11-11 05:30:00+03	0.00
2	1	3	2020-12-02 21:13:57+03	30000.00
3	7	2	2021-09-30 18:31:10+03	10000.50
4	6	4	2021-09-01 06:37:43+03	100000.87
5	9	1	2019-02-15 09:43:58+03	0.00
6	1	4	2019-12-08 16:27:00+03	150000.15
7	5	2	2021-05-23 20:16:27+03	1000.00
8	6	4	2020-07-30 01:05:21+03	327434.27
9	7	3	2020-01-27 02:11:11+03	400.00
10	1	4	2019-08-12 07:54:48+03	3000.00
\.


--
-- Data for Name: operation_type; Type: TABLE DATA; Schema: public; Owner: dr_eremin
--

COPY public.operation_type (id, operation_name) FROM stdin;
1	Запрос баланса
2	Пополнение счета
3	Снятие денег со счета
4	Перевод денег со счета на счет
\.


--
-- Data for Name: passport; Type: TABLE DATA; Schema: public; Owner: dr_eremin
--

COPY public.passport (id, client_id, series, passport_number, date_issue, whom_issued_by, code_division) FROM stdin;
1	1	1231	123456	1995-02-12	ОТДЕЛЕНИЕ УФМС РОССИИ ПО СТАВРОПОЛЬСКОМУ КРАЮ В АЛЕКСАНДРОВСКОМ Р-НЕ	260-002
2	2	2342	234567	1992-07-07	ОТДЕЛЕНИЕ УФМС РОССИИ ПО ХАБАРОВСКОМУ КРАЮ В Р-НЕ ИМЕНИ ЛАЗO	270-017
3	3	3453	345678	1998-03-08	ОТДЕЛЕНИЕ УФМС РОССИИ ПО КРАСНОЯРСКОМУ КРАЮ В ЦЕНТРАЛЬНОМ Р-НЕ Г. КРАСНОЯРСКА	240-020
4	4	4564	456789	1992-06-05	ТЕПЛООЗЕРСКИЙ ОМ ОВД ОБЛУЧЕНСКОГО Р-НА ЕВРЕЙСКОЙ АО	793-006
5	5	5675	567890	1999-02-14	ОТДЕЛЕНИЕ УФМС РОССИИ ПО ПЕРМСКОМУ КРАЮ В КРАСНОВИШЕРСКОМ Р-НЕ	590-031
6	7	7897	789012	2001-01-09	1 ПО ПВС УВД Г. ПЕТРОПАВЛОВСКА-КАМЧАТСКОГО	412-001
\.


--
-- Data for Name: phone; Type: TABLE DATA; Schema: public; Owner: dr_eremin
--

COPY public.phone (id, phone_number) FROM stdin;
4	+7(921)111-11-11
5	+7(911)222-22-22
6	+7(921)333-33-33
7	+7(981)444-44-44
8	+7(905)555-55-55
9	+7(911)666-66-66
10	+7(960)777-77-77
11	+7(921)888-88-88
\.


--
-- Data for Name: phone_client; Type: TABLE DATA; Schema: public; Owner: dr_eremin
--

COPY public.phone_client (phone_id, client_id) FROM stdin;
4	1
5	2
6	3
7	4
8	5
9	6
10	7
11	4
\.


--
-- Data for Name: transfer_recipient; Type: TABLE DATA; Schema: public; Owner: dr_eremin
--

COPY public.transfer_recipient (recipient_account_id, operation_id) FROM stdin;
4	4
5	6
9	8
5	10
\.


--
-- Name: bank_account_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dr_eremin
--

SELECT pg_catalog.setval('public.bank_account_id_seq', 10, true);


--
-- Name: client_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dr_eremin
--

SELECT pg_catalog.setval('public.client_id_seq', 7, true);


--
-- Name: email_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dr_eremin
--

SELECT pg_catalog.setval('public.email_id_seq', 7, true);


--
-- Name: operation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dr_eremin
--

SELECT pg_catalog.setval('public.operation_id_seq', 10, true);


--
-- Name: operation_type_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dr_eremin
--

SELECT pg_catalog.setval('public.operation_type_id_seq', 4, true);


--
-- Name: passport_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dr_eremin
--

SELECT pg_catalog.setval('public.passport_id_seq', 6, true);


--
-- Name: phone_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dr_eremin
--

SELECT pg_catalog.setval('public.phone_id_seq', 11, true);


--
-- Name: bank_account bank_account_pkey; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.bank_account
    ADD CONSTRAINT bank_account_pkey PRIMARY KEY (id);


--
-- Name: client client_pkey; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT client_pkey PRIMARY KEY (id);


--
-- Name: email_client email_client_email_id_key; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.email_client
    ADD CONSTRAINT email_client_email_id_key UNIQUE (email_id);


--
-- Name: email_client email_client_pkey; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.email_client
    ADD CONSTRAINT email_client_pkey PRIMARY KEY (email_id, client_id);


--
-- Name: email email_email_address_key; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.email
    ADD CONSTRAINT email_email_address_key UNIQUE (email_address);


--
-- Name: email email_pkey; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.email
    ADD CONSTRAINT email_pkey PRIMARY KEY (id);


--
-- Name: operation operation_pkey; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.operation
    ADD CONSTRAINT operation_pkey PRIMARY KEY (id);


--
-- Name: operation_type operation_type_pkey; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.operation_type
    ADD CONSTRAINT operation_type_pkey PRIMARY KEY (id);


--
-- Name: passport passport_client_id_key; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.passport
    ADD CONSTRAINT passport_client_id_key UNIQUE (client_id);


--
-- Name: passport passport_pkey; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.passport
    ADD CONSTRAINT passport_pkey PRIMARY KEY (id);


--
-- Name: passport passport_series_passport_number_key; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.passport
    ADD CONSTRAINT passport_series_passport_number_key UNIQUE (series, passport_number);


--
-- Name: phone_client phone_client_phone_id_key; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.phone_client
    ADD CONSTRAINT phone_client_phone_id_key UNIQUE (phone_id);


--
-- Name: phone phone_phone_number_key; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.phone
    ADD CONSTRAINT phone_phone_number_key UNIQUE (phone_number);


--
-- Name: phone phone_pkey; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.phone
    ADD CONSTRAINT phone_pkey PRIMARY KEY (id);


--
-- Name: transfer_recipient transfer_recepient_operation_id_unique_fkey; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.transfer_recipient
    ADD CONSTRAINT transfer_recepient_operation_id_unique_fkey UNIQUE (recipient_account_id, operation_id);


--
-- Name: transfer_recipient transfer_recepient_pkey; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.transfer_recipient
    ADD CONSTRAINT transfer_recepient_pkey PRIMARY KEY (recipient_account_id, operation_id);


--
-- Name: bank_account user_id_unique; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.bank_account
    ADD CONSTRAINT user_id_unique UNIQUE (client_id);


--
-- Name: bank_account bank_account_client_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.bank_account
    ADD CONSTRAINT bank_account_client_id_fkey FOREIGN KEY (client_id) REFERENCES public.client(id) ON DELETE CASCADE;


--
-- Name: email_client email_client_client_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.email_client
    ADD CONSTRAINT email_client_client_id_fkey FOREIGN KEY (client_id) REFERENCES public.client(id) ON DELETE CASCADE;


--
-- Name: email_client email_client_email_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.email_client
    ADD CONSTRAINT email_client_email_id_fkey FOREIGN KEY (email_id) REFERENCES public.email(id) ON DELETE CASCADE;


--
-- Name: operation operation_account_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.operation
    ADD CONSTRAINT operation_account_id_fkey FOREIGN KEY (account_id) REFERENCES public.bank_account(id) ON DELETE CASCADE;


--
-- Name: operation operation_operation_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.operation
    ADD CONSTRAINT operation_operation_type_id_fkey FOREIGN KEY (operation_type_id) REFERENCES public.operation_type(id) ON DELETE CASCADE;


--
-- Name: passport passport_client_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.passport
    ADD CONSTRAINT passport_client_id_fkey FOREIGN KEY (client_id) REFERENCES public.client(id) ON DELETE CASCADE;


--
-- Name: phone_client phone_client_client_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.phone_client
    ADD CONSTRAINT phone_client_client_id_fkey FOREIGN KEY (client_id) REFERENCES public.client(id) ON DELETE CASCADE;


--
-- Name: phone_client phone_client_phone_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.phone_client
    ADD CONSTRAINT phone_client_phone_id_fkey FOREIGN KEY (phone_id) REFERENCES public.phone(id) ON DELETE CASCADE;


--
-- Name: transfer_recipient transfer_recepient_account_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.transfer_recipient
    ADD CONSTRAINT transfer_recepient_account_id_fkey FOREIGN KEY (recipient_account_id) REFERENCES public.bank_account(id) ON DELETE CASCADE;


--
-- Name: transfer_recipient transfer_recepient_operation_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.transfer_recipient
    ADD CONSTRAINT transfer_recepient_operation_id_fkey FOREIGN KEY (operation_id) REFERENCES public.operation(id) ON DELETE CASCADE;


--
-- PostgreSQL database dump complete
--

