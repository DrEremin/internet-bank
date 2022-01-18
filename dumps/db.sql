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
    user_id bigint NOT NULL,
    current_balance numeric(20,2) DEFAULT 0.00 NOT NULL,
    CONSTRAINT positive_id CHECK ((user_id > 0))
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
-- Name: bank_account id; Type: DEFAULT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.bank_account ALTER COLUMN id SET DEFAULT nextval('public.bank_account_id_seq'::regclass);


--
-- Data for Name: bank_account; Type: TABLE DATA; Schema: public; Owner: dr_eremin
--

COPY public.bank_account (id, user_id, current_balance) FROM stdin;
1	2	1001.05
2	1	756.00
5	5	0.00
4	10	0.01
6	120	100000355.00
\.


--
-- Name: bank_account_id_seq; Type: SEQUENCE SET; Schema: public; Owner: dr_eremin
--

SELECT pg_catalog.setval('public.bank_account_id_seq', 6, true);


--
-- Name: bank_account bank_account_pkey; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.bank_account
    ADD CONSTRAINT bank_account_pkey PRIMARY KEY (id);


--
-- Name: bank_account user_id_unique; Type: CONSTRAINT; Schema: public; Owner: dr_eremin
--

ALTER TABLE ONLY public.bank_account
    ADD CONSTRAINT user_id_unique UNIQUE (user_id);


--
-- PostgreSQL database dump complete
--

