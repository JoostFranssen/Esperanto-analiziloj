class Analizaĵoj extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            elektitaIndico: null,
            komponentoj: null,
        }
    }

    render() {
        return (
            <div className="vort-analizaĵujo">
                {this.kreiKomponentojn()}
            </div>
        );
    }

    kreiKomponentojn() {
        let komponentListo = [];
        if(this.props.analizaĵoj) {
            for(let i = 0; i < this.props.analizaĵoj.length; i++) {
                komponentListo.push(
                    <Analizaĵo
                        analizaĵo={this.props.analizaĵoj[i]}
                        elektita={this.state.elektitaIndico === i}
                        onClick={() => this.elektiKomponenton(i)}
                    />
                );
            }
        }

        this.state.komponentoj = komponentListo;

        if(komponentListo.length > 0 && !this.state.elektitaIndico) {
            this.elektiKomponenton(0);
        }

        return komponentListo;
    }

    elektiKomponenton(indico) {
        let newState = Object.assign({}, this.state);

        if(indico < 0 || !this.state.komponentoj || indico >= this.state.komponentoj.length) {
            newState.elektitaIndico = null;
        } else {
            newState.elektitaIndico = indico;
        }

        if(this.state.elektitaIndico != indico) {
            this.setState(newState);
        }
    }
}