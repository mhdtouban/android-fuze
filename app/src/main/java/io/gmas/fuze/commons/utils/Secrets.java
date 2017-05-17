package io.gmas.fuze.commons.utils;

public class Secrets {

    public class Api {
        public class Endpoint {
            public static final String PRODUCTION = "https://jsonplaceholder.typicode.com";
            public static final  String DEVELOPMENT = "https://jsonplaceholder.typicode.com";
        }
    }

    public class SharedPreference {
        public class Key {
            public static final String KEY_AUTH_TOKEN = "authToken";
            public static final String KEY_EXPIRE = "expire";
            public static final String KEY_EMAIL = "email";
        }
    }

}
