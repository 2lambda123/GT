<script>
    import { onMount } from "svelte";

    const apiURL = "http://localhost:8084/order/all";
    const inputData = {"symbol":"lulu","quantity":500};
    const inputDataToMatch = {"symbol":"lulu","quantity":500};

    let idToDelete = 0;

    let outputData = [];

    const getAllOrders = async () => {
        const { hostname: location } = window.location;
        const response = await fetch(`http://${location}:8084/order/all/`);
       if (!response.ok) throw Error(response.message);

       try {
          outputData = await response.json();
          console.log(outputData);
       } catch (err) {
         throw err;
       }
     };

    const addOrder = async () => {
      const { hostname: location } = window.location;
       const settings = {
         method: 'POST',
         body: JSON.stringify(inputData),
         headers: {
           'Accept': 'application/json',
           'Content-Type': 'application/json',
         }
       }

       const response = await fetch(`http://${location}:8084/order/add/`, settings);
       if (!response.ok) throw Error(response.message);

       try {
         const data = await response.data;
         console.log(data);
       } catch (err) {
         throw err;
       }
     };

     const matchOrder = async () => {
      const { hostname: location } = window.location;
       const settings = {
         method: 'POST',
         body: JSON.stringify(inputDataToMatch),
         headers: {
           'Accept': 'application/json',
           'Content-Type': 'application/json',
         }
       }

       const response = await fetch(`http://${location}:8084/order/match/`, settings);
       if (!response.ok) throw Error(response.message);

       try {
         const data = await response.data;
         console.log(data);
       } catch (err) {
         throw err;
       }
     };

     const deleteOrder = async () => {
      const { hostname: location } = window.location;
       const settings = {
         method: 'DELETE',
       }

       const response = await fetch(`http://${location}:8084/order/delete/` + idToDelete, settings);
       if (!response.ok) throw Error(response.message);

       try {
         const data = await response.data;
         console.log(data);
       } catch (err) {
         throw err;
       }
     };
</script>

<main>
  <button on:click={getAllOrders}>
    Get All Orders
  </button>

<button on:click={addOrder}>
	Add An Order
</button>

<button on:click={matchOrder}>
	Match An Order
</button>

<input bind:value={idToDelete} placeholder="Order ID #">
<button on:click={deleteOrder}>
	Delete Order #{idToDelete}
</button>

{#each outputData as item }
    <div>
        <p> {item.id} </p>
        <p> {item.symbol} </p>
        <p> {item.quantity} </p>
        <p> --------------- </p>
    </div>
{/each}
</main>