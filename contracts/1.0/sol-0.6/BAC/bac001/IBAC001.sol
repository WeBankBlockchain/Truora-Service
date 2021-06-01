pragma solidity ^0.6.6;


interface IBAC001 {

    function totalAmount() external view returns (uint256);

    function balance(address owner) external view returns (uint256);

    function send(address to, uint256 value, bytes calldata data) external ;

    function sendFrom(address from, address to, uint256 value, bytes calldata data) external;

    function allowance(address owner, address spender) external view returns (uint256);

    function approve(address spender, uint256 amount) external returns (bool);

    function destroy(uint256 value, bytes calldata data) external;

    function destroyFrom(address from, uint256 value, bytes calldata data) external;

    function issue(address to, uint256 value, bytes calldata data) external  returns (bool);

    function batchSend(address[] calldata to, uint256[] calldata values, bytes calldata data) external;

    function increaseAllowance(address spender, uint256 addedValue) external  returns (bool);

    function decreaseAllowance(address spender, uint256 subtractedValue) external  returns (bool);

    event Send(address indexed from, address indexed to, uint256 value, bytes data);

    event Approval(address indexed owner, address indexed spender, uint256 value);

}