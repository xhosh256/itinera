import Header from "../components/Header.jsx";
import {Link, useNavigate} from "react-router-dom";
import Input from "../components/Input.jsx";
import {useContext, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";

const RegisterPage = () => {
    const {setUser} = useContext(AuthContext);
    const navigate = useNavigate();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        const response = await fetch(
            "http://localhost:8080/api/v1/auth/register",
            {
                credentials: "include",
                method: "POST",
                headers: {"content-type": "application/json"},
                body: JSON.stringify({username, password, email})
            }
        );

        const user = await response.json();
        setUser(user);
        navigate("/");
    }

    return (
        <div>
            <Header />
            <div>
                <form onSubmit={handleSubmit}>
                    <Input
                        id="username"
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    >Имя пользователя</Input>

                    <Input
                        id="password"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    >Пароль</Input>

                    <Input
                        id="email"
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    >Элекронная почта</Input>

                    <button type="submit" >Зарегестрироваться</button>
                </form>
                <p>Уже есть аккаунт? <Link to="/auth/login">Войти</Link> </p>
            </div>

        </div>
    )
}

export default RegisterPage