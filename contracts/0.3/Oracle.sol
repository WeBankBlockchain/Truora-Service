

pragma solidity  ^0.4.24;
contract OracleCoreI {

    address public cbAddress;
    function query(uint _timestamp, string  _datasource, string  _arg) external  returns (bytes32 _id);

}


contract usingOracleCore {


    OracleCoreI oracleCoreI;


    function oracle_setNetwork(address oracleAddress) public returns (bool _networkSet) {
        if (getCodeSize(oracleAddress) > 0) {

            oracleCoreI = OracleCoreI(oracleAddress);

            return true;
        }

        return false;
    }

    function __callback(bytes32 _myid, string memory _result) public {
         _myid; _result;
    }

    function oracle_query(string memory _datasource, string memory _arg)  internal returns (bytes32 _id) {

        return oracleCoreI.query(0, _datasource, _arg);
    }

    function oracle_query(uint _timestamp, string memory _datasource, string memory _arg)  internal returns (bytes32 _id) {

        return oracleCoreI.query(_timestamp, _datasource, _arg);
    }


    function oracle_cbAddress()  internal returns (address _callbackAddress) {
        return oracleCoreI.cbAddress();
    }

    function getCodeSize(address _addr) view internal returns (uint _size) {
        assembly {
            _size := extcodesize(_addr)
        }
    }


}

