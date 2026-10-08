import Header from "../components/Header.jsx";
import {Link} from "react-router-dom";
import Input from "../components/Input.jsx";
import {useState} from "react";

const RegisterPage = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        const response = await fetch(
            "http://localhost:8080/api/v1/auth/register",
            {
                method: "POST",
                headers: {"content-type": "application/json"},
                body: JSON.stringify({username, password, email})
            }
        );

        const data = await response.json();
        console.log(data);
    }

    return (
        <div>
            <Header />
            <div>
                <form onSubmit={handleSubmit}>
                    <Input
                        id="username"
                        label="Имя пользователя"
                        type="text"
                        onChange={(e) => setUsername(e.target.value)}
                    />

                    <Input
                        id="password"
                        label="Пароль"
                        type="password"
                        onChange={(e) => setPassword(e.target.value)}
                    />

                    <Input
                        id="email"
                        label="Элекронная почта"
                        type="email"
                        onChange={(e) => setEmail(e.target.value)}
                    />

                    <button type="submit" >Зарегестрироваться</button>
                </form>
                <p>Already have an account? <Link to="/auth/login">Log in</Link> </p>
            </div>

        </div>
    )
}

export default RegisterPage