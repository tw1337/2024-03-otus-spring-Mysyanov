import React, { Component } from "react";
import { Container } from "reactstrap";
import AppNavbar from "./AppNavbar";

class BookComments extends Component {
    constructor(props) {
        super(props);
        this.state = { comments: [], book: "" };
    }

    async componentDidMount() {
        if (this.props.match.params.id !== "new") {
            const comments = await (
                await fetch(`/api/books/${this.props.match.params.id}/comments`)
            ).json();
            const book = await (
                await fetch(`/api/books/${this.props.match.params.id}`)
            ).json();
            this.setState({ comments: comments, book: book });
        }
    }

    render() {
        const { book, comments } = this.state;

        if (!book || !comments) {
            return <p>Fetching data...</p>;
        }

        return (
            <div>
                <AppNavbar />
                <Container fluid>
                    <div>
                        <dl>
                            <dt>Book Title</dt>
                            <dd>{book.title}</dd>
                            <dt>Book Author</dt>
                            <dd>{book.author.fullName}</dd>
                            <dt>Book Genres</dt>
                            <dd>
                                {book.genres.map((genre) => genre.name).join()}
                            </dd>
                        </dl>
                        <b>Comments:</b>
                        <ul>
                            {comments.map((comment) => {
                                return <li> {comment.text} </li>;
                            })}
                        </ul>
                    </div>
                </Container>
            </div>
        );
    }
}

export default BookComments;
