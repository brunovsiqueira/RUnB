package br.unb.runb.models;

public class User {

    //     "client": "public",
    //    "access_token": "1noq6aHg1vTmmVGoJTSpP2dqkq0Tshj2",
    //    "expires_in": 3600,
    //    "resource_owner": {
    //        "id": 201210118,
    //        "remap_user_id": 58674,
    //        "codigo": 586653,
    //        "matricula": "160122791",
    //        "name": "Giuliana da Cunha Facciolli",
    //        "email": "giulianafacciolli@hotmail.com",
    //        "type": 3,
    //        "subtype": 2,
    //        "isActive": true,
    //        "cpf": "05604588199",
    //        "lista_perfil": [],
    //        "lista_permission": [],
    //        "lista_perfil_permission": []
    //    }

    private String id; //id é usado para consulta de saldo
    private String accessToken;
    private String codigo;
    private String matricula;
    private String name;
    private String email;
    private boolean isActive;
    private String cpf;
    //TODO: What does type and subtype means?


    public static User userObj;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public static User getInstance () {
        if (userObj == null) {
            userObj = new User();
        }
        return userObj;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
