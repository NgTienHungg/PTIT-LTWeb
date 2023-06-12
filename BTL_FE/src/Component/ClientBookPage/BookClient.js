import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import "./BookClient.css"
import moment from "moment";

const BookClient = () => {
  const params = useParams();
  const id = params.id;

  const [bookItem, setBookItem] = useState({});
  const [quantity, setQuantity] = useState(1);
  const [rating, setRating] = useState(5);
  const [comment, setComment] = useState("");
  const [reviews, setReviews] = useState([]);

  useEffect(() => {
    fetch(`http://localhost:8080/book/${id}`)
      .then((response) => response.json())
      .then((data) => setBookItem(data))
      .catch((err) => console.log(err));

    fetch(`http://localhost:8080/rating/idBook?idBook=${id}`)
      .then((response) => response.json())
      .then((data) => setReviews(data))
      .catch((err) => console.log(err));
  }, []);

  // Xử lý thêm vào giỏ hàng
  const handleAddToCart = () => {
    const order = {
      idUser: localStorage.getItem("userId"),
      idBook: bookItem.id,
      sum: quantity,
    };

    console.log(order);

    // Thực hiện gửi yêu cầu đặt hàng đến máy chủ
    fetch("http://localhost:8080/orders", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(order)
    })
      .then((response) => {
        if (response.ok) {
          alert("Mua thành công!");
          console.log("Order placed successfully");
        } else {
          alert("Failed to place order");
          console.log("Failed to place order");
        }
      })
      .catch((error) => console.log("Error:", error));
  };

  const handleQuantityChange = (event) => {
    setQuantity(parseInt(event.target.value));
  };

  const handleRatingChange = (event) => {
    setRating(parseInt(event.target.value));
  };

  const handleCommentChange = (event) => {
    setComment(event.target.value);
  };

  const handleAddReview = () => {
    // validate rating và comment
    if (rating === 0) {
      alert("Vui lòng chọn mức độ yêu thích!")
      return;
    }

    if (comment.trim() === "") {
      alert("Nhập bình luận trước khi đánh giá!");
      return;
    }

    // tạo review mới
    const newReview = {
      idBook: id,
      idUser: localStorage.getItem("userId"),
      starCnt: rating,
      comment: comment,
      userName: "user"
    };

    // gửi dữ liệu lên server
    fetch("http://localhost:8080/rating", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(newReview)
    })
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
        setReviews([...reviews, data]);
        setRating(5);
        setComment("");
      })
      .catch((error) => {
        console.log("Error:", error);
      });
  };


  // npm install moment: chuyển date từ sql sang dạng dd/mm/yyyy
  function formatMySQLDate(dateFromMySQL) {
    const formattedDate = moment(dateFromMySQL).format("DD/MM/YYYY");
    return formattedDate;
  }


  return (
    <>
      <br />
      <div className="container">
        <div className="row">
          <div className="col-3">
            <img
              src={bookItem.cover}
              alt={bookItem.title}
              className="img-fluid"
            />
          </div>
          <div className="col-9">
            <h2>{bookItem.title}</h2>
            <p>Tác giả: {bookItem.author}</p>
            <p>Thể loại: {bookItem.category}</p>
            <p>Số trang: {bookItem.pageNumber}</p>
            <p>Ngày xuất bản: {bookItem.releaseDate && formatMySQLDate(bookItem.releaseDate)}</p>

            {
              localStorage.getItem("token") !== null && (
                <div >
                  <div className="d-flex align-items-center">
                    <input
                      className="quantity-purchased"
                      type="number"
                      id="quantity"
                      min="1"
                      value={quantity}
                      onChange={handleQuantityChange}
                    />
                  </div>
                  <button className="btn btn-success" onClick={handleAddToCart}>Đặt mua</button>
                </div>
              )}
          </div>
        </div>

        <br />
        <h5>Mô tả</h5>
        <p className="form-control-static bordered">
          {
            bookItem.description
              ? bookItem.description
              : "..."
          }
        </p>

        <h5>Đánh giá</h5>
        <div className="row">
          <div className="col-12">
            <div className="row">
              <div className="col-12">
                <ul className="list-group">
                  {
                    reviews.map((review) => (
                      <li className="list-group-item mb-3" key={review.id}>
                        <div className="d-flex align-items-center">
                          <h5 className="mr-2">{review.userName}</h5>
                          <p>({review.starCnt}/5*)</p>
                        </div>
                        <p>{review.comment}</p>
                      </li>
                    ))}
                </ul>
              </div>
            </div>

            {
              localStorage.getItem("token") === "client" && (
                <div className="row mb-3">
                  <div className="col-12">

                    <div className="form-group">
                      <select
                        className="form-control col-1"
                        id="rating" value={rating}
                        onChange={handleRatingChange}
                      >
                        <option value="5">5 stars</option>
                        <option value="4">4 stars</option>
                        <option value="3">3 stars</option>
                        <option value="2">2 stars</option>
                        <option value="1">1 star</option>
                      </select>
                    </div>

                    <div className="form-group">
                      <textarea
                        className="form-control"
                        id="comment" rows="3"
                        placeholder="Nhập bình luận của bạn..."
                        value={comment}
                        onChange={handleCommentChange}
                      />
                    </div>

                    <button className="btn btn-primary" onClick={handleAddReview}>Đánh giá</button>
                  </div>
                </div>
              )
            }
          </div>
        </div>
      </div>
    </>
  );
};

export default BookClient;
