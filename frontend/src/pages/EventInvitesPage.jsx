import Header from "../components/Header.jsx";
import {useContext, useEffect, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";
import './EventInvitesPage.css'
import {NavLink} from "react-router-dom";
import Pagination from "../components/Pagination.jsx";

const EventInvitesPage = () => {

    const {user, setUser } = useContext(AuthContext);

    const [invitations, setInvitations] = useState([]);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [tab, setTab] = useState("incoming");

    useEffect(() => {
        if(!user) return;

        const fetchInvitations = async () => {
            const response = await fetch(`http://localhost:8080/api/v1/invitations?page=${page-1}`, {
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
            setTotalPages(data.totalPages);
        }

        fetchInvitations();
    }, [user, page])

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
        <div className="events__invite__page">
            <Header/>
            <div className="events__invite__page__layout">
                <div className="events__invite__sidebar">
                    <button
                        className={tab === "incoming" ? "active" : ""}
                        onClick={() => setTab("incoming")}>Входящие</button>
                    <button
                        className={tab === "outcoming" ? "active" : ""}
                        onClick={() => setTab("outcoming")}>Исходящие</button>
                </div>

                <div className="events__invite__page__content">
                    <h1>Приглашения</h1>
                    {tab === "incoming" ? (
                        <div>
                            <div className="events__invite__page__items">
                                {invitations.length >= 1 ? invitations.map((invitation) => (
                                    <div key={invitation.id} className="events__invite">
                                        <div className="events__invite__header">
                                            <p>{invitation.eventName}</p>
                                            <p className={invitation.visibility === 'PUBLIC' ? 'public' : 'private'}>{invitation.visibility}</p>
                                        </div>
                                        {invitation.status === 'PENDING' && (
                                            <div className="events__invite__buttons">
                                                <button onClick={() => handleAccept(invitation.id)}>Принять</button>
                                                <button onClick={() => handleDecline(invitation.id)}>Отклонить</button>
                                            </div>
                                        )}
                                    </div>
                                )) : <p>Приглашений пока что нет)</p>}

                                <Pagination
                                    page={page}
                                    totalPages={totalPages}
                                    onPageChange={setPage}
                                />
                            </div>
                        </div>

                    ) : (
                        <div>
                            Нету
                        </div>
                    )}

                </div>

            </div>
        </div>
    )
}

export default EventInvitesPage