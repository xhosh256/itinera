import Header from "../components/Header.jsx";
import {Link, useNavigate} from "react-router-dom";
import Input from "../components/Input.jsx";
import {useContext, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";

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

        console.log(response.status);
        const meResponse = await fetch(
            "http://localhost:8080/api/v1/users/me",
            {
                credentials: "include"
            }
        );

        const user = await meResponse.json();
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
                        label="Username"
                        type="text"
                        onChange={(e) => setUsername(e.target.value)}
                    />

                    <Input
                        id="password"
                        label="Password"
                        type="password"
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <button type="submit">Войти</button>
                </form>
                <p>Don't have an account? <Link to="/auth/register">Register now</Link> </p>
            </div>

        </div>
    )
}

export default LoginPage;