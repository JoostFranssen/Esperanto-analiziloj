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
                    componentList.push(
                        <Frazero
                            frazero={frazero}
                            onClick={this.handleFrazeroClick.bind(this)}
                        />
                    );
                }
            }

            let propertiesComponent = null;
            if(this.state.currentFrazero) {
                propertiesComponent = <div
                    className="frazer-properties"
                    >
                        {snakeCaseToTitleCase(this.state.currentFrazero.funkcio)}
                </div>
            }

            return (
                <div
                    className="frazo"
                >
                    {componentList}
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