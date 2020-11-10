var db = connect("mongodb://usuario:senha@localhost:27017/admin");

db = db.getSiblingDB('newstack');

db.createUser(
    {
        user: "regular",
        pwd: "senha",
        roles: [ { role: "readWrite", db: "newstack"} ],
        passwordDigestor: "server",
    }
)
