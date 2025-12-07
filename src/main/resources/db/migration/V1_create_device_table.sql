CREATE TABLE device (
                        uuid DEFAULT gen_random_uuid()   PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        brand VARCHAR(255) NOT NULL,
                        state VARCHAR(20) NOT NULL,
                        creation_time TIMESTAMP WITH TIME ZONE NOT NULL
);