class VortAnalizilo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            analizaĵoj: null,
            vorto: null,
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
                >
                    <label id="vorto-etikedo" for="vort-enmeto">
                        Analizota vorto
                    </label>
                    <input
                        type="text"
                        name="vorto"
                        id="vort-enmeto"
                        autocomplete="off"
                        autoCapitalize="off"
                        spellcheck="false"
                        pattern="^((?=[a-zA-ZĉĈĝĜĥĤĵĴŝŜŭŬ])[^qQw-yW-Y])+$"
                        required
                        autoFocus
                        onInvalid={
                            (event) => {
                                if(event.target.value === "" || /\s/.test(event.target.value)) {
                                    event.target.setCustomValidity("Bonvolu enmeti unu Esperantan vorton");
                                } else {
                                    event.target.setCustomValidity('Bonvolu uzi nur Esperantajn literojn.')}
                                }
                            }
                        onInput={(event) => event.target.setCustomValidity('')}
                    />
                    <input
                        type="submit"
                        value="Analizi"
                        id="konfirm-butono"
                    />
                </form>
                {this.bildigiAnalizaĵojn()}
            </div>
        );
    }

    async traktiKonfirmon(event) {
        event.preventDefault();
        let enmeto = document.getElementById("vort-enmeto");
        let response = await fetch(`api/vortanalizo?vorto=${enmeto.value}`);
        let result = await response.json();

        this.setState({
            analizaĵoj: result.analizaĵoj,
            vorto: enmeto.value,
        });
    }

    bildigiAnalizaĵojn() {
        return <Analizaĵoj key={this.state.vorto} analizaĵoj={this.state.analizaĵoj}/>;
    }
}
