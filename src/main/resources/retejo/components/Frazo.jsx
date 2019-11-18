class Frazo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            currentFrazero: null,
        }
    }

    render() {
        if(this.props.frazo) {
            let frazeroj = this.props.frazo.frazeroj;
            let componentList = [];
            if(frazeroj) {
                for(const frazero of frazeroj) {
                    componentList.push(<span>&nbsp;</span>);
                    componentList.push(
                        <Frazero
                            frazero={frazero}
                            onClick={this.handleFrazeroClick.bind(this)}
                            selected={frazero === this.state.currentFrazero}
                        />
                    );
                }
            }

            let propertiesComponent = null;
            if(frazeroj.includes(this.state.currentFrazero)) {
                propertiesComponent = <div
                    className="frazer-properties"
                    key={this.state.currentFrazero}
                    >
                        {snakeCaseToTitleCase(this.state.currentFrazero.funkcio)}
                </div>
            }

            return (
                <div
                    className="frazo"
                >
                    {componentList.slice(1)}
                    {propertiesComponent}
                </div>
            );
        } else {
            return null;
        }
    }

    handleFrazeroClick(frazero) {
        this.setState({
            currentFrazero: frazero,
        });
    }
}