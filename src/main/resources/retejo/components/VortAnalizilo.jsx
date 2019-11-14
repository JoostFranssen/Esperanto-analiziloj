class VortAnalizilo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            analizaĵoj: null,
            vorto: null,
            errorMessage: null,
            loading: false,
        }
    }

    render() {
        return (
            <div id="vort-analizilo">
                <h1>Vorto-Analizilo</h1>
                <form
                    onSubmit={(event) => this.traktiKonfirmon(event)}
                    id="vort-analizilo-form"
                    autocomplete="off"
                    novalidate
                >
                    <label id="vort-label" for="vort-input">
                        Analizota vorto
                    </label>
                    <input
                        type="text"
                        name="vorto"
                        id="vort-input"
                        autocomplete="off"
                        autoCapitalize="off"
                        spellcheck="false"
                        pattern="^((?=[a-zA-ZĉĈĝĜĥĤĵĴŝŜŭŬ])[^qQw-yW-Y])+$"
                        autoFocus
                        novalidate
                        onInput={
                            (event) => {
                                let newState = Object.assign({}, this.state);
                                if(!event.target.validity.valid) {
                                    if(/\s/.test(event.target.value)) {
                                        newState.errorMessage = "Enmetu ekzakte unu vorton";
                                    } else {
                                        newState.errorMessage = "Uzu nur Esperantajn literojn"
                                    }
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
                        id="confirm-button"
                    />
                    <span id="vort-error-message">{this.state.errorMessage}</span>
                    <span id="vort-loading" style={{display: this.state.loading ? "block" : "none"}}></span>
                </form>
                {this.bildigiAnalizaĵojn()}
            </div>
        );
    }

    async traktiKonfirmon(event) {
        event.preventDefault();

        let newState = Object.assign({}, this.state);
        newState.loading = true;
        this.setState(newState);

        let enmeto = document.getElementById("vort-input");
        let response = await fetch(`api/vortanalizo?vorto=${enmeto.value}`);
        let result = await response.json();

        this.setState({
            analizaĵoj: result.analizaĵoj,
            vorto: enmeto.value,
            errorMessage: null,
            loading: false,
        });
    }

    bildigiAnalizaĵojn() {
        return <Analizaĵoj key={this.state.vorto} analizaĵoj={this.state.analizaĵoj}/>;
    }
}
