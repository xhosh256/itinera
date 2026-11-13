import {useContext, useEffect, useState} from "react";
import AuthContext from "../context/AuthContext.jsx";
import Header from "../components/Header.jsx";
import {useNavigate, useParams} from "react-router-dom";

const InviteFriendsToEvent = () => {

    const navigate = useNavigate();

    const {eventId} = useParams();

    const {user, setUser} = useContext(AuthContext);

    const [friends, setFriends] = useState([]);

    const [selected, setSelected] = useState([]);

    useEffect(() => {
        if(!user) return;

        const fetchFriends = async () => {
            const response = await fetch(`http://localhost:8080/api/v1/friendships/friends/${user.id}`, {
                method: 'GET',
                credentials: 'include',
                headers: {
                    'Content-Type': 'application/json',
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
            setFriends(data.content);
        }

        fetchFriends();
    }, [user])

    const handleSelect = (e) => {
        const value = Number(e.target.value);
        console.log(value);

        setSelected(prev => prev.includes(value) ? prev.filter(item => item !== value) : [...prev, value]);
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (selected.length === 0) return;

        const responses = await Promise.all(
            selected.map(id =>
                fetch(`http://localhost:8080/api/v1/events/${eventId}/invitations/${id}`, {
                    method: 'POST',
                    credentials: 'include'
                })
            )
        );

        if (responses.some(response => response.status === 401)) {
            setUser(null);
            return;
        }

        if (responses.some(response => !response.ok)) {
            throw new Error('Не удалось отправить некоторые приглашения');
        }

        navigate(`/events/${eventId}`);
    }

    return (
        <div>
            <Header/>

            <form onSubmit={handleSubmit}>
                {friends.length > 0 ? (
                    friends.map((friend) => (
                        <div key={friend.id}>
                            <p>{friend.firstname} {friend.lastname}</p>
                            <input
                                value={friend.userId}
                                type="checkbox"
                                onChange={handleSelect}
                                checked={selected.includes(friend.userId)}
                            />
                        </div>
                    ))
                ) : (<div>У вас нет друзей :(</div>)}
                <button>Пригласить</button>
            </form>

        </div>
    );
}

export default InviteFriendsToEvent;