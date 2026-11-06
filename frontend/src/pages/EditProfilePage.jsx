import Header from "../components/Header.jsx";
import {useContext, useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import AuthContext from "../context/AuthContext.jsx";
import Input from "../components/Input.jsx";

const EditProfilePage = () => {

    const {profileId} = useParams();
    const {user, setUser} = useContext(AuthContext);

    const [firstname, setFirstname] = useState("");
    const [lastname, setLastname] = useState("");
    const [birthDate, setBirthDate] = useState(null);
    const [bio, setBio] = useState("");

    const navigate = useNavigate();

    useEffect(() => {
        if(!user) return;

        const fetchProfile = async () => {
            const response = await fetch(`http://localhost:8080/api/v1/profiles/${user.id}`, {
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
            setFirstname(data.firstname);
            setLastname(data.lastname);
            setBio(data.bio);
            setBirthDate(data.birthDate);
        }

        fetchProfile();
    }, [user]);

    if(!user) return (
        <p>Загрузка...</p>
    );

    const handleEditProfile = async (e) => {
        e.preventDefault();

        const response = await fetch(`http://localhost:8080/api/v1/profiles/${profileId}`, {
            method: "PUT",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({firstname, lastname, birthDate, bio}),
        });

        if(response.status === 401) {
            setUser(null);
            return;
        }

        if(!response.ok) {
            throw new Error(response.statusText);
        }

        navigate(`/profile/${user.id}`);
    }

    return (
        <div>
            <Header/>

            <form onSubmit={handleEditProfile}>
                <Input
                    id="firstname"
                    value={firstname}
                    type="text"
                    onChange={(e) => setFirstname(e.target.value)}
                >
                    Имя
                </Input>

                <Input
                    id="lastname"
                    value={lastname}
                    type="text"
                    onChange={(e) => setLastname(e.target.value)}
                >
                    Фамилия
                </Input>

                <Input
                    id="firstname"
                    value={birthDate}
                    type="date"
                    onChange={(e) => setBirthDate(e.target.value)}
                >
                    День Рождения
                </Input>

                <Input
                    id="bio"
                    value={bio}
                    type="text"
                    onChange={(e) => setBio(e.target.value)}
                >
                    О себе
                </Input>

                <button>Сохранить</button>
            </form>
        </div>
    )
}

export default EditProfilePage;