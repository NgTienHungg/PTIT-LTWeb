import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import "./Book.css";

export const Book = () => {
  const params = useParams();
  const id = params.id;

  const [bookItem, setBookItem] = useState({});
  const [categories, setCategories] = useState([]);

  const [isEditing, setIsEditing] = useState(id < 0 ? true : false);
  const [isAdding, setIsAdding] = useState(id < 0 ? true : false);

  // đường dẫn ảnh tạm thời
  const [uploadedImage, setUploadedImage] = useState("")

  useEffect(() => {
    console.log("editting: " + isEditing);
    console.log("adding: " + isAdding);

    fetch(`http://localhost:8080/book/${id}`)
      .then((response) => response.json())
      .then((data) => {
        setBookItem(data);
        setUploadedImage(data.cover); // set cover cho uploadedImage để hiển thị ra khi 
      })
      .catch((err) => console.log(err));

    fetch("http://localhost:8080/category")
      .then((response) => response.json())
      .then((data) => setCategories(data))
      .catch((err) => console.log(err));
  }, []);


  const handleImageChange = (e) => {
    const file = e.target.files[0];
    const reader = new FileReader();
    reader.onloadend = () => {
      setBookItem({ ...bookItem, cover: reader.result });
      console.log("img url:" + bookItem.cover);
    };

    if (file) {
      reader.readAsDataURL(file);
      setUploadedImage(URL.createObjectURL(file));
    }
  };

  const handleEditing = () => {
    setIsEditing(true);
  }

  const handleUpdate = () => {
    // validate dữ liệu
    if (!bookItem.title || !bookItem.author || !bookItem.releaseDate) {
      if (!bookItem.title) alert("Tiêu đề không được để trống!");
      else if (!bookItem.author) alert("Tác giả không được để trống!");
      else alert("Hãy chọn ngày phát hành!");
      return;
    }

    setIsEditing(false);

    // Tạo một đối tượng chứa dữ liệu cần cập nhật
    const updatedData = {
      title: bookItem.title,
      author: bookItem.author,
      categoryId: bookItem.categoryId,
      category: bookItem.category,
      releaseDate: bookItem.releaseDate,
      pageNumber: bookItem.pageNumber,
      soldNumber: bookItem.soldNumber,
      cover: bookItem.cover,
      description: bookItem.description,
    };

    // Gửi dữ liệu cập nhật lên server
    fetch(`http://localhost:8080/book/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(updatedData),
    })
      .then((response) => response.json())
      .then((data) => console.log("Data updated:", data))
      .catch((error) => console.error("Error:", error));
  };


  const handleAddNew = () => {
    // validate dữ liệu
    if (!bookItem.title || !bookItem.author || !bookItem.releaseDate) {
      if (!bookItem.title) alert("Tiêu đề không được để trống!");
      else if (!bookItem.author) alert("Tác giả không được để trống!");
      else alert("Hãy chọn ngày phát hành!");
      return;
    }

    const confirmed = window.confirm(
      "Thêm cuốn sách này vào thư viện?"
    );

    if (confirmed) {
      setIsAdding(false);

      // Tạo một đối tượng chứa dữ liệu mới
      const newData = {
        title: bookItem.title,
        author: bookItem.author,
        categoryId: bookItem.categoryId,
        category: bookItem.category,
        releaseDate: bookItem.releaseDate,
        pageNumber: bookItem.pageNumber,
        soldNumber: 0,
        cover: bookItem.cover,
        description: bookItem.description,
      };

      console.log("new data: " + newData);

      // Gửi dữ liệu mới lên server
      fetch("http://localhost:8080/book", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(newData),
      })
        .then((response) => response.json())
        .then((data) => window.location.href = `/admin/book/${data.id}`)
        .catch((error) => console.error("Error:", error));
    }
  };


  return (
    <div className="Bookdetail">
      <div className="container">
        <div className="row">
          <div className="col-6">
            <div className="title-author d-flex">
              <div className="title">
                <label class="required" htmlFor="Text">Tiêu đề</label>
                <input
                  type="text"
                  name="title"
                  id="title"
                  value={bookItem.title}
                  onChange={(e) =>
                    setBookItem({ ...bookItem, title: e.target.value })
                  }
                  disabled={!isEditing}
                />
              </div>

              <div className="author">
                <label class="required" htmlFor="Text">Tác giả</label>
                <input
                  type="text"
                  name="author"
                  id="author"
                  value={bookItem.author}
                  onChange={(e) =>
                    setBookItem({ ...bookItem, author: e.target.value })
                  }
                  disabled={!isEditing}
                />
              </div>
            </div>

            <div className="description">
              <label htmlFor="Text">Mô tả</label>
              <div className="textarea-container">
                <textarea
                  name="description"
                  id="description"
                  value={bookItem.description}
                  onChange={(e) =>
                    setBookItem({ ...bookItem, description: e.target.value })
                  }
                  disabled={!isEditing}
                />
              </div>
            </div>

            <div className="date-pageNumber d-flex">
              <div className="date">
                <label class="required" htmlFor="text">Ngày phát hành</label>
                <input
                  className="text-center"
                  type="date"
                  name="releasedate"
                  id="releasedate"
                  value={bookItem.releaseDate}
                  onChange={(e) =>
                    setBookItem({ ...bookItem, releaseDate: e.target.value })
                  }
                  disabled={!isEditing}
                />
              </div>

              <div className="pagenumber">
                <label htmlFor="text">Số trang</label>
                <input
                  className="text-center"
                  type="text"
                  name="pageNumber"
                  id="pageNumber"
                  value={bookItem.pageNumber}
                  onChange={(e) =>
                    setBookItem({ ...bookItem, pageNumber: e.target.value })
                  }
                  disabled={!isEditing}
                />
              </div>
            </div>

            <div className="category">
              <label htmlFor="text">Thể loại</label>
              {isEditing ? (
                <select
                  value={bookItem.category}
                  onChange={(e) => {
                    const selectedCategoryId = e.target.value;
                    const selectedCategory = categories.find((category) => String(category.id) === selectedCategoryId);
                    const categoryName = selectedCategory
                      ? selectedCategory.name
                      : "none";
                    setBookItem({
                      ...bookItem,
                      categoryId: selectedCategoryId,
                      category: categoryName,
                    });
                  }}
                >
                  <option value="">{bookItem.category}</option>
                  {
                    // duyệt lần lượt các phần tử trong categories
                    // tạo option với key và value là categoryId
                    categories.map((category) => (
                      <option key={category.id} value={category.id}>
                        {category.name}
                      </option>
                    ))
                  }
                </select>
              ) : (
                <input
                  type="text"
                  name="category"
                  id="category"
                  value={bookItem.category}
                  onChange={(e) => setBookItem({ ...bookItem, category: e.target.value })}
                  disabled={!isEditing}
                />
              )}
            </div>
          </div>

          <div className="col-4 text-center">
            <div className="cover d-flex flex-column">
              <label htmlFor="Text">{id < 0 ? "" : `Trang bìa`}</label>
              {
                isEditing ? (
                  <div className="upload-modal">
                    {uploadedImage && <img src={uploadedImage} alt="Uploaded" style={{ maxWidth: "800px", maxHeight: "300px", objectFit: "contain" }} />}
                    <input
                      className="img-file"
                      type="file"
                      accept="image/*"
                      onChange={handleImageChange}
                    />
                  </div>
                ) : (
                  <>
                    {
                      //TODO: Kiểm tra xem có ảnh chưa và show, chưa có thì hiện btn Edit Cover
                      bookItem.cover ? (
                        <img
                          className="cover"
                          src={bookItem.cover}
                          alt="bìa sách"
                        />
                      ) : (
                        <p>Chưa có ảnh nào được tải lên!</p>
                      )
                    }
                  </>
                )}
            </div>
          </div>
        </div>
      </div>

      <div className="footer" style={{ display: "flex", justifyContent: "flex-end" }}>
        {isAdding ? (
          <button class="btn btn-success" style={{ marginRight: `30px`, width: '80px' }} onClick={handleAddNew}>
            Add
          </button>
        ) : (
          <>
            {isEditing ? (
              <button class="btn btn-success" style={{ marginRight: `30px`, width: '80px' }} onClick={handleUpdate}>
                Update
              </button>
            ) : (
              localStorage.getItem("token") === "admin" && (
                <button class="btn btn-warning" style={{ marginRight: `30px`, width: '80px' }} onClick={handleEditing}>
                  Edit
                </button>
              )
            )}
          </>
        )}
        <div className="footer-decoration"></div>
      </div>
    </div>
  );
};

export default Book;
