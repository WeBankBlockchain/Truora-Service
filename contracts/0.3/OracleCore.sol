
pragma solidity ^0.4.24;


contract OracleCore {
    mapping (address => uint) reqc;

    
    address public cbAddress;

    event Log1(address sender, bytes32 cid, uint timestamp, string datasource, string arg);
   
    address owner;

    modifier onlyadmin {
        if ((msg.sender != owner)&&(msg.sender != cbAddress)) throw;
       _;
    }

    function setCBaddress(address newCbaddress) {
      if (msg.sender != owner || newCbaddress == 0) throw;
      cbAddress = newCbaddress;
    }

  
    function() onlyadmin {}

     constructor() {
        owner = msg.sender;
        cbAddress = msg.sender;
        
    }


    function query(string _datasource, string _arg)  returns (bytes32 _id) {
        return query1(0, _datasource, _arg);
    }



    function query(uint _timestamp, string _datasource, string _arg) returns (bytes32 _id) {
        return query1(_timestamp, _datasource, _arg);
    }
  

    function query1(uint _timestamp, string _datasource, string _arg)  returns (bytes32 _id) {
        if ((_timestamp > now+3600*24*60)) throw;
        _id = sha3( this, msg.sender, reqc[msg.sender]);
        reqc[msg.sender]++;
        Log1(msg.sender, _id, _timestamp, _datasource, _arg);
        return _id;
    }

}