import {createContext, useEffect, useState} from "react";

const AuthContext = createContext(null);

const AuthProvider = ({ children }) => {

    const [user, setUser] = useState(null);

    useEffect(() => {
        fetch("http://localhost:8080/api/v1/users/me", {
            credentials: "include"
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error();
                }

                return response.json();
            })
            .then(data => {
                setUser(data);
            })
            .catch(() => {
                setUser(null);
            });
    }, [])

    return (
        <AuthContext.Provider value={{ user, setUser }}>
            {children}
        </AuthContext.Provider>
    )
}

export {AuthProvider};
export default AuthContext;