function verifyPassword() {
    var password = document.getElementById("password").value;
    var pwc =document.getElementById("confirmPassword").value;
    var div = document.getElementById("warning");
    if(div!=null)div.remove();
    if (password == "") {
        div=makeDiv();
        div.appendChild(document.createTextNode("Введите пароль"));
        let current = document.getElementById("passwordForm");
        document.getElementById("form").insertBefore(div, current);
        return false;
    } 
    if (password.length < 4) {
        div=makeDiv();
        div.appendChild(document.createTextNode("Пароль должен быть болле 4 символов"));
        let current = document.getElementById("passwordForm");
        document.getElementById("form").insertBefore(div, current);
        return false;
    }
    if (password.length > 15) {
        div=makeDiv();
        div.appendChild(document.createTextNode("Пароль должен быть менее 15 символов"));
        let current = document.getElementById("passwordForm");
        document.getElementById("form").insertBefore(div, current);
        return false;
    }
    if (password != pwc) {
        div=makeDiv();
        div.appendChild(document.createTextNode("Пароли не совпадают"));
        let current = document.getElementById("passwordForm");
        document.getElementById("form").insertBefore(div, current);
        return false;
    }
    return true;
}
function makeDiv(){
    div = document.createElement("div");
    div.id="warning";
    div.className="bg-danger border rounded text-white text-center";
    return div;
}