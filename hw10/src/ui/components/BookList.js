import React, { Component } from "react";
import { Button, ButtonGroup, Container, Table } from "reactstrap";
import AppNavbar from "./AppNavbar";
import { Link } from "react-router-dom";

class BookList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            books: [],
        };
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        fetch("/api/books")
            .then((response) => response.json())
            .then((data) => this.setState({ books: data }));
    }

    async remove(id) {
        await fetch(`/api/books/${id}`, {
            method: "DELETE",
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
            },
        }).then(() => {
            let updatedBooks = [...this.state.books].filter((i) => i.id !== id);
            this.setState({ books: updatedBooks });
        });
    }

    render() {
        const { books } = this.state;

        const BookList = books.map((book) => {
            return (
                <tr key={book.id}>
                    <td style={{ whiteSpace: "nowrap" }}>{book.title}</td>
                    <td style={{ whiteSpace: "nowrap" }}>
                        {book.author.fullName}
                    </td>
                    <td style={{ whiteSpace: "nowrap" }}>
                        {book.genres.map((genre) => genre.name).join()}
                    </td>
                    <td>
                        <Button
                            size="sm"
                            color="link"
                            tag={Link}
                            to={"/books/comments/" + book.id}
                        >
                            Show Comments
                        </Button>
                    </td>
                    <td>
                        <ButtonGroup>
                            <Button
                                size="sm"
                                color="primary"
                                tag={Link}
                                to={"/books/" + book.id}
                            >
                                Edit
                            </Button>
                            <Button
                                size="sm"
                                color="danger"
                                onClick={() => this.remove(book.id)}
                            >
                                Delete
                            </Button>
                        </ButtonGroup>
                    </td>
                </tr>
            );
        });

        return (
            <div>
                <AppNavbar />
                <Container fluid>
                    <div className="mt-4 float-right">
                        <Button color="success" tag={Link} to="/books/new">
                            Add Book
                        </Button>
                    </div>
                    <div className="mt-4">
                        <h3>Books</h3>
                    </div>
                    <Table className="mt-4">
                        <thead>
                            <tr>
                                <th width="20%">Title</th>
                                <th width="20%">Author</th>
                                <th width="20%">Genres</th>
                                <th width="20%">Comments</th>
                                <th width="20%">Actions</th>
                            </tr>
                        </thead>
                        <tbody>{BookList}</tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}

export default BookList;
