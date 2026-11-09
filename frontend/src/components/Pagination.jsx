import './Pagination.css'
import { FaCaretRight, FaCaretLeft } from "react-icons/fa6";

const Pagination = ({page, totalPages, onPageChange}) => {
    if (totalPages <= 1) {
        return null
    }

    const pages = Array.from(
        {length: totalPages},
        (_, i) => i + 1,
    );

    return (
        <nav className="pagination">
            <button disabled={page === 1} onClick={() => onPageChange(page - 1)} >
                <FaCaretLeft size={20} />
            </button>

            {pages.map((number) => (
                <button
                    key={number}
                    onClick={() => onPageChange(number)}
                    className={number === page ? "active" : ""}
                >
                    {number}
                </button>
            ))}

            <button disabled={page===totalPages} onClick={() => onPageChange(page+1)}>
                <FaCaretRight size={20} />
            </button>
        </nav>
    )
}

export default Pagination;