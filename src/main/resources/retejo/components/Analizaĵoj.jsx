class Analizaĵoj extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        let komponentListo = [];
        if(this.props.vortoj) {
            for(const vorto of this.props.vortoj) {
                komponentListo.push(<Analizaĵo vorto={vorto} />);
            }
        }
        return (
            <div className="vort-analizaĵujo">
                {komponentListo}
            </div>
        );
    }
}