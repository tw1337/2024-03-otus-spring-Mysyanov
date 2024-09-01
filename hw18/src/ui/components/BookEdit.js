import React, { Component } from "react";
import { Link, withRouter } from "react-router-dom";
import { Button, Container, Form, FormGroup, Input, Label } from "reactstrap";
import Select from "react-select";
import AppNavbar from "./AppNavbar";

class BookEdit extends Component {
    emptyBook = {
        title: "",
        authorId: "",
        genres: [],
    };

    constructor(props) {
        super(props);
        this.state = {
            changedBook: this.emptyBook,
            book: this.emptyBook,
            authors: [],
            genres: [],
        };
        this.handleTitleChange = this.handleTitleChange.bind(this);
        this.handleSelectAuthorChange = this.handleSelectAuthorChange.bind(this);
        this.handleSelectGenreChange = this.handleSelectGenreChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async componentDidMount() {
        const authors = await (await fetch(`/api/authors`)).json();
        const genres = await (await fetch(`/api/genres`)).json();
        let book = "";
        if (this.props.match.params.id !== "new") {
            book = await (
                await fetch(`/api/books/${this.props.match.params.id}`)
            ).json();
        }

        this.setState({
            book: book,
            authors: authors,
            genres: genres,
            changedBook: this.emptyBook,
        });
    }

    handleTitleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let book = { ...this.state.book };
        let changedBook = { ...this.state.changedBook };
        let authors = this.state.authors;
        let genres = this.state.genres;
        changedBook[name] = value;
        this.setState({
            book: book,
            changedBook: changedBook,
            authors: authors,
            genres: genres,
        });
    }

    handleSelectAuthorChange(event) {
        const value = event.value;
        let book = { ...this.state.book };
        let changedBook = { ...this.state.changedBook };
        let authors = this.state.authors;
        let genres = this.state.genres;
        changedBook["authorId"] = value;
        this.setState({
            book: book,
            changedBook: changedBook,
            authors: authors,
            genres: genres,
        });
    }

    handleSelectGenreChange(values) {
        let book = { ...this.state.book };
        let changedBook = { ...this.state.changedBook };
        let authors = this.state.authors;
        let genres = this.state.genres;

        let genreIds = [];
        values.forEach((item, i) => {
            genreIds.push(item.value);
        });

        changedBook["genres"] = genreIds;
        this.setState({
            book: book,
            changedBook: changedBook,
            authors: authors,
            genres: genres,
        });
    }

    async handleSubmit(event) {
        event.preventDefault();
        const { book, changedBook } = this.state;

        let title = changedBook.title ? changedBook.title : book.title;
        let authorId = changedBook.authorId
            ? changedBook.authorId
            : book.author.id;

        let genres = [];
        if (changedBook.genres && changedBook.genres.length > 0) {
            genres = changedBook.genres;
        } else {
            book.genres.forEach((item, i) => {
                genres.push(item.id);
            });
        }
        const bookToSend = {
            id: book.id,
            title: title,
            authorId: authorId,
            genres: genres,
        };

        await fetch("/api/books" + (bookToSend.id ? "/" + bookToSend.id : ""), {
            method: bookToSend.id ? "PATCH" : "POST",
            headers: {
                Accept: "application/json",
                "Content-Type": "application/json",
            },
            body: JSON.stringify(bookToSend),
        });
        this.props.history.push("/books");
    }

    render() {
        const { book, authors, genres } = this.state;
        if (!authors || authors.length == 0 || !genres || genres.length == 0) {
            return <p>Fetching data...</p>;
        }

        const title = <h2>{book.id ? "Edit Book" : "Add Book"}</h2>;

        let authorOptions = [];
        authors.forEach((author, i) => {
            authorOptions.push({
                value: author.id,
                label: author.fullName,
            });
        });

        let defaultAuthor = [];
        if (book.author) {
            defaultAuthor.push({
                value: book.author.id,
                label: book.author.fullName,
            });
        }

        let genreOptions = [];
        genres.forEach((genre, i) => {
            genreOptions.push({
                value: genre.id,
                label: genre.name,
            });
        });

        let defaultGenre = [];
        if (book.genres) {
            book.genres.forEach((genre, i) => {
                defaultGenre.push({
                    value: genre.id,
                    label: genre.name,
                });
            });
        }

        return (
            <div>
                <AppNavbar />
                <Container>
                    {title}
                    <Form onSubmit={this.handleSubmit}>
                        <FormGroup>
                            <Label for="title">Title</Label>
                            <Input
                                type="text"
                                name="title"
                                id="title"
                                defaultValue={book.title || ""}
                                onChange={this.handleTitleChange}
                            />
                        </FormGroup>
                        <FormGroup>
                            <Label for="authorId">Author</Label>
                            <Select
                                id="authorId"
                                name="authorId"
                                options={authorOptions}
                                defaultValue={defaultAuthor}
                                onChange={this.handleSelectAuthorChange}
                            />
                        </FormGroup>
                        <FormGroup>
                            <Label for="genre">Genre</Label>
                            <Select
                                isMulti
                                id="genre"
                                name="genre"
                                options={genreOptions}
                                defaultValue={defaultGenre}
                                onChange={this.handleSelectGenreChange}
                            />
                        </FormGroup>
                        <FormGroup>
                            <Button color="primary" type="submit">
                                Save
                            </Button>{" "}
                            <Button color="secondary" tag={Link} to="/books">
                                Cancel
                            </Button>
                        </FormGroup>
                    </Form>
                </Container>
            </div>
        );
    }
}

export default withRouter(BookEdit);
