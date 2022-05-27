/* Bootstrapping testing data */
INSERT INTO users (username, "password", native_lang_id, chosen_lang_id) VALUES
    ('mockUser', 'not-a-real-pasword', 11, 14);
INSERT INTO user_langs (user_id, lang_id) VALUES (2, 14);
INSERT INTO texts (title, text, user_id, lang_id) VALUES
    ('SQL short',
     'SQL is a domain-specific language used in programming and designed for managing data held in a relational database management system (RDBMS), or for stream processing in a relational data stream management system (RDSMS). It is particularly useful in handling structured data, i.e. data incorporating relations among entities and variables.\\n\\nSQL offers three main advantages over older read–write APIs such as ISAM or VSAM. Firstly, it introduced the concept of accessing many records with one single command. Secondly, it eliminates the need to specify how to reach a record, e.g. with or without an index. Finally, SQL uses a human-readable syntax that allows users to be quickly productive without a requirement for long-term, technical training.',
     2,
     14);