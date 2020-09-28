import React, { Component } from "react";
import axios from "axios";
import Table from "react-bootstrap/Table";

class WalletView extends Component {
  state = {
    wallets: [],
  };

  componentDidMount() {
    const config = {
      method: "get",
      url: "http://localhost:8080/api/v1/wallet",
      headers: {
        Authorization: "Basic dXNlcjpwYXNz",
      },
    };

    axios(config).then((res) => {
      const wallets = res.data;
      this.setState({ wallets });
    });
  }

  render() {
    let rows = [];

    this.state.wallets.forEach((wallet) => {
      const row = (
        <tr key={wallet.id}>
          <td>{wallet.id}</td>
          <td>{wallet.name}</td>
          <td>{wallet.fund_amount}</td>
        </tr>
      );
      rows.push(row);
    });

    return (
      <div>
        View All Wallet
        <Table striped bordered hover size="sm">
          <thead>
            <tr>
              <th>#</th>
              <th>Wallet Name</th>
              <th>Available Fund</th>
            </tr>
          </thead>
          <tbody>{rows}</tbody>
        </Table>
      </div>
    );
  }
}

export default WalletView;
