import moment from "moment";
import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import Header from "../Header/Header";

const BookTable = () => {
  const [books, setBooks] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8080/book")
      .then((response) => response.json())
      .then((data) => setBooks(data))
      .catch((err) => console.log(err));
  }, []);

  // npm install moment: chuyển date từ sql sang dạng dd/mm/yyyy
  function formatMySQLDate(dateFromMySQL) {
    const formattedDate = moment(dateFromMySQL).format("DD/MM/YYYY");
    return formattedDate;
  }

  const handleDelete = (id) => {
    const confirmed = window.confirm("Bạn có chắc là muốn xoá cuốn sách này?");

    if (confirmed) {
      fetch(`http://localhost:8080/book/${id}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((response) => {
          if (response.ok) {
            alert("Xoá thành công!");
            const remainingBooks = books.filter((item) => item.id !== id); // Thực hiện cập nhật danh sách sau khi xóa
            setBooks(remainingBooks);
          } else {
            // Xóa thất bại
            console.log("Failed to delete book");
            alert("Xoá thất bại!");
          }
        })
        .catch((error) => {
          console.log("Error deleting book:", error);
        });
    }
  };

  return (
    <>
      <Header />
      <br />
      <div className="container" >
        <table className="table table-striped table-bordered table-custom ">
          <thead className="table-dark">
            <tr>
              <th className="text-center align-middle">Title</th>
              <th className="text-center align-middle">Author</th>
              <th className="text-center align-middle">Category</th>
              <th className="text-center align-middle">Release Date</th>
              <th className="text-center align-middle">Page Number</th>
              <th className="text-center align-middle">Sold Number</th>
              <th className="text-center align-middle">Action</th>
            </tr>
          </thead>
          <tbody>{
            books.map((book) => (
              <tr key={book.id}>
                <td className="text-center align-middle">{book.title}</td>
                <td className="text-center align-middle">{book.author}</td>
                <td className="text-center align-middle">{book.category}</td>
                <td className="text-center align-middle">{formatMySQLDate(book.releaseDate)}</td>
                <td className="text-center align-middle">{book.pageNumber}</td>
                <td className="text-center align-middle">{book.soldNumber}</td>

                <td className="text-center align-middle width">{
                  // Hiện 2 nút action nếu là admin
                  localStorage.getItem("token") === "admin" && (
                    <div className="d-flex flex-column">
                      <Link to={`/admin/book/${book.id}`} className="btn btn-info">
                        View
                      </Link>
                      <button className="btn btn-danger" onClick={() => handleDelete(book.id)}>
                        Delete
                      </button>
                    </div>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <br /> <br /> <br />

        <footer className="footer">
          <div className="col-12 text-right">
            {localStorage.getItem("token") === "admin" && (
              <Link to={`/admin/book/-1`} className="btn btn-primary">
                Add book
              </Link>
            )}
          </div>
        </footer>
      </div>
    </>
  );
};

export default BookTable;
