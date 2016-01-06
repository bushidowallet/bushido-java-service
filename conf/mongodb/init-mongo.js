db = connect('localhost:27017/admin');
db.dropAllUsers();
db.createUser({
 user: 'bushidoadmin',
 pwd: '40dss03sla2ASs!@sbitc$W',
 roles: [{ role: 'userAdminAnyDatabase', db: 'admin' }]
});
db.auth('bushidoadmin', '40dss03sla2ASs!@sbitc$W');
db.getSiblingDB('bushido');
db.dropAllUsers();
db.createUser({
 user: 'jo0hny24',
 pwd: 'crap36Aubd',
 roles: [{ role: 'readWrite', db: 'bushido' }]
});
