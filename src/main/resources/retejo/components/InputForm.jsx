class InputForm extends React.Component {
    constructor(props) {
        super(props);
        /*
         * handleConfirm: redonu, ĉu rezultoj estis akiritaj
         * label: etikedo de la formularo
         * pattern: ŝablono por valideco de la enmeto
         * handleInvalidity: se la enmeto estas nevalida, redonu la erarmesaĝon (en diversaj kazoj)
         * errorMessageForNoResults: la erarmesaĝo, kiam ne estis rezultoj
         */
        this.state = {
            errorMessage: "",
            loading: false,
        }
    }

    render() {
        return (
            <form
                onSubmit={this.handleConfirm.bind(this)}
                className="analizilo-form"
                autocomplete="off"
                novalidate
            >
                <label className="input-label" for="input">
                    {this.props.label}
                </label>
                <input
                    type="text"
                    name="vorto"
                    id="input"
                    className="input"
                    autocomplete="off"
                    autoCapitalize="off"
                    spellcheck="false"
                    pattern={this.props.pattern}
                    autoFocus
                    novalidate
                    onInput={
                        (event) => {
                            let newState = Object.assign({}, this.state);
                            if(!event.target.validity.valid) {
                                newState.errorMessage = this.props.handleInvalidity(event.target.value);
                            } else {
                                newState.errorMessage = "";
                            }
                            this.setState(newState);
                        }
                    }
                    onInvalid={(event) => event.preventDefault()}
                />
                <input
                    type="submit"
                    value="Analizi"
                    className="confirm-button"
                />
                <span className="error-message">{this.state.errorMessage}</span>
                <span className="loading" style={{display: this.state.loading ? "block" : "none"}}></span>
            </form>
        );
    }

    async handleConfirm(event) {
        event.preventDefault();

        let input = document.getElementById("input");

        let newState = Object.assign({}, this.state);
        newState.loading = true;
        this.setState(newState);

        let resultObtained = await this.props.handleConfirm(input.value);

        this.setState({
            errorMessage: !resultObtained ? this.props.errorMessageForNoResults : "",
            loading: false,
        });
    }
}