server {

    listen 80;
    listen [::]:80;
    server_name app.bushidowallet.com;
    rewrite ^/(.*) https://app.bushidowallet.com/$1 permanent;	
}

server {
	listen 443;
	server_name app.bushidowallet.com;

	root /var/www/app.bushidowallet.com/html;
	index index.html index.htm;

	ssl on;
	ssl_certificate /etc/nginx/sites-available/cert/bundle.crt;
	ssl_certificate_key /etc/nginx/sites-available/cert/star_bushidowallet_com.key;

	ssl_session_timeout 5m;

        ssl_protocols TLSv1.1 TLSv1.2;
        ssl_ciphers 'ECDHE-RSA-AES128-GCM-SHA256:ECDHE-ECDSA-AES128-GCM-SHA256:ECDHE-RSA-AES256-GCM-SHA384:ECDHE-ECDSA-AES256-GCM-SHA384:DHE-RSA-AES128-GCM-SHA256:DHE-DSS-AES128-GCM-SHA256:kEDH+AESGCM:ECDHE-RSA-AES128-SHA256:ECDHE-ECDSA-AES128-SHA256:ECDHE-RSA-AES128-SHA:ECDHE-ECDSA-AES128-SHA:ECDHE-RSA-AES256-SHA384:ECDHE-ECDSA-AES256-SHA384:ECDHE-RSA-AES256-SHA:ECDHE-ECDSA-AES256-SHA:DHE-RSA-AES128-SHA256:DHE-RSA-AES128-SHA:DHE-DSS-AES128-SHA256:DHE-RSA-AES256-SHA256:DHE-DSS-AES256-SHA:DHE-RSA-AES256-SHA:!aNULL:!eNULL:!EXPORT:!DES:!RC4:!3DES:!MD5:!PSK';
        ssl_prefer_server_ciphers on;

        if ( $request_filename ~ /index.html ) {
          rewrite ^ http://app.bushidowallet.com/login.html permanent;
        }

	    location = / {
          rewrite "^.*$" http://app.bushidowallet.com/login.html break;
	    }

        location /login {
          root /var/www/app.bushidowallet.com/html/modules/signin;            
        }

        location /register {
          root /var/www/app.bushidowallet.com/html/modules/signup;       
        }

        location /help {
          root /var/www/app.bushidowallet.com/html/modules/signin/help;            
        }

        location /verify {
          root /var/www/app.bushidowallet.com/html/modules/account/verify;
        }

        location /confirm {
          root /var/www/app.bushidowallet.com/html/modules/account/verify/confirm;
        }

        location /checkout {
          root /var/www/app.bushidowallet.com/html/modules/checkout;
        }

        location /docs {
          root /var/www/app.bushidowallet.com/html/modules/docs;
        }

        location /export {
          root /var/www/app.bushidowallet.com/html/modules/export;
        }

        location /password {
          root /var/www/app.bushidowallet.com/html/modules/password;
        }

        location /pos {
          root /var/www/app.bushidowallet.com/html/modules/pos;
        }

        location /settings {
          root /var/www/app.bushidowallet.com/html/modules/settings;
        }

        location /setup {
          root /var/www/app.bushidowallet.com/html/modules/setup;
        }

        location /transactions {
          root /var/www/app.bushidowallet.com/html/modules/transactions;
        }

        location /user {
          root /var/www/app.bushidowallet.com/html/modules/user;
        }

        location /wallet {
          root /var/www/app.bushidowallet.com/html/modules/wallet;
        }
}
