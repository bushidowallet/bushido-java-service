#!/bin/sh

#follow the - after security enabled path...
mongo --host 127.0.0.1 --port 27017

use admin
db.createUser(
  {
    user: "bushidoAdmin",
    pwd: "bushidoPass",
    roles: [ { role: "userAdminAnyDatabase", db: "admin" } ]
  }
)
exit

#authenticate as admin
mongo --port 27017 -u bushidoAdmin -p bushidoPass --authenticationDatabase admin