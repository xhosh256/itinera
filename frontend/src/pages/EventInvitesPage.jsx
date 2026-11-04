import Header from "../components/Header.jsx";
import {useContext, useEffect, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";

const EventInvitesPage = () => {

    const {user, setUser } = useContext(AuthContext);

    const [invitations, setInvitations] = useState([]);

    useEffect(() => {
        if(!user) return;

        const fetchInvitations = async () => {
            const response = await fetch(`http://localhost:8080/api/v1/invitations`, {
                method: "GET",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                }
            });

            if(response.status === 401) {
                setUser(null);
                return;
            }

            if(!response.ok) {
                throw new Error(response.statusText);
            }

            const data = await response.json();
            setInvitations(data.content);
        }

        fetchInvitations();
    }, [user])

    if(!user) {
        return <h1>Пользователь не найден</h1>
    }

    const handleAccept = async (id) => {
        const response = await fetch(`http://localhost:8080/api/v1/invitations/${id}/accept`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            }
        });

        if(response.status === 401) {
            setUser(null);
            return;
        }

        if(!response.ok) {
            throw new Error(response.statusText);
        }

        setInvitations(prev => prev.filter(item => item.id !== id));
    }

    const handleDecline = async (id) => {
        const response = await fetch(`http://localhost:8080/api/v1/invitations/${id}/decline`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            }
        });

        if(response.status === 401) {
            setUser(null);
            return;
        }

        if(!response.ok) {
            throw new Error(response.statusText);
        }

        setInvitations(prev => prev.filter(item => item.id !== id));
    }

    return (
        <div>
            <Header/>
            <div>
                <h1>Приглашения</h1>
                {invitations.length >= 1 ? invitations.map((invitation) => (
                    <div key={invitation.id}>
                        <p>Название события: {invitation.eventName}</p>
                        <p>Видимость события: {invitation.visibility}</p>
                        {invitation.status === 'PENDING' && (
                            <div>
                                <button onClick={() => handleAccept(invitation.id)}>Принять</button>
                                <button onClick={() => handleDecline(invitation.id)}>Отклонить</button>
                            </div>
                        )}
                    </div>
                )) : <p>Приглашений пока что нет)</p>}
            </div>
        </div>
    )
}

export default EventInvitesPage