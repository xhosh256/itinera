const Pagination = ({page, totalPages, onPageChange}) => {
    if (totalPages <= 1) {
        return null
    }

    const pages = Array.from(
        {length: totalPages},
        (_, i) => i + 1,
    );

    return (
        <nav>
            <button disabled={page === 1} onClick={() => onPageChange(page - 1)} >
                ◁
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
                ▷
            </button>
        </nav>
    )
}

export default Pagination;