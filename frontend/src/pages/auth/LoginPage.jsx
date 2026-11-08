import Header from "../../components/Header.jsx";
import {Link, useNavigate} from "react-router-dom";
import Input from "../../components/Input.jsx";
import {useContext, useState} from "react";
import AuthContext from "../../context/AuthContext.jsx";
import './LoginPage.css'

const LoginPage = () => {
    const { setUser } = useContext(AuthContext);
    const navigate = useNavigate();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();

        const response = await fetch(
            "http://localhost:8080/api/v1/auth/login",
            {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                credentials: "include",
                body: JSON.stringify({username, password}),
            }
        )

        const user = await response.json();
        setUser(user);
        navigate("/");
    }

    return (
        <div className="login__page">
            <Header />
            <div className="register__page__layout">
                <form className="login__form" onSubmit={handleSubmit}>
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
                    > Пароль </Input>
                    <button type="submit">Войти</button>
                </form>
                <p>Ещё нет аккаунта? <Link to="/auth/register">Регистрация</Link> </p>
            </div>

        </div>
    )
}

export default LoginPage;