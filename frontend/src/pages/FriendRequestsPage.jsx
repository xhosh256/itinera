import Header from "../components/Header.jsx";
import {useContext, useEffect, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";
import LoginPage from "./LoginPage.jsx";

const FriendRequestsPage = () => {

    const {user, setUser} = useContext(AuthContext);

    const [requests, setRequests] = useState([]);

    useEffect(() => {
        if(!user) return;

        const fetchRequests = async () => {
            const response = await fetch(`http://localhost:8080/api/v1/friendships/requests/${user.id}`, {
                method: 'GET',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
                }
            })

            if(response.status === 401) {
                setUser(null);
                setUser(null);
            }

            if(!response.ok) {
                throw new Error(response.statusText);
            }

            const data = await response.json();
            setRequests(data);
        }

        fetchRequests();
    }, [user]);

    if(!user) {
        return <LoginPage/>
    }

    return (
        <div>
            <Header />
            <h1>Заявки в друзья</h1>

            {requests.length === 0 ?
                <h1>Заявок не найдено :(</h1> :
                requests.map(({id, user}) => (
                    <div key={id}>
                        <p>{user.firstname} {user.lastname}</p>
                        <button>Принять</button>
                        <button>Отклонить</button>
                    </div>
                ))
            }

        </div>
    )
}

export default FriendRequestsPage;