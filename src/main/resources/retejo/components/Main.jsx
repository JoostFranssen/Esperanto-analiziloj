class Main extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedIndex: null,
        }
    }

    render() {
        return (
            <div>
                <NavigationBar passNewSelected={this.updateSelected.bind(this)}/>
                <div style={this.displayOnIndex(0)}>
                    <VortAnalizilo />
                </div>
            </div>
        );
    }

    updateSelected(index) {
        this.setState({selectedIndex: index});
    }

    displayOnIndex(index) {
        return {display: (this.state.selectedIndex === index ? "block" : "none")};
    }
}