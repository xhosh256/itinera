import {useContext, useState} from "react";
import {useNavigate} from "react-router-dom";

const SearchBar = (props) => {
    const {
        children,
    } = props;

    const [name, setName] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        navigate(`/events/search?name=${encodeURIComponent(name)}`);
    }

    return (
        <div>
            <form onSubmit={handleSubmit}>
                <input placeholder={children} onChange={(e) => setName(e.target.value)}/>
                <button>Найти</button>
            </form>
        </div>
    )
}

export default SearchBar;