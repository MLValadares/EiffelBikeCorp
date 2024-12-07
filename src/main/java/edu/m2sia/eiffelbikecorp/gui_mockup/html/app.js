const API_BASE_URL = 'http://localhost:8080/EiffelBikeCorp_war_exploded/api';
let authToken = null;

function showLogin() {
    document.getElementById('login-form').classList.remove('hidden');
    document.getElementById('register-form').classList.add('hidden');
}

function showRegister() {
    document.getElementById('login-form').classList.add('hidden');
    document.getElementById('register-form').classList.remove('hidden');
}

async function login() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        const response = await axios.post(`${API_BASE_URL}/users/login`, { username, password });
        authToken = response.data.token;
        
        document.getElementById('auth-section').classList.add('hidden');
        document.getElementById('main-content').classList.remove('hidden');
        
        loadRentBikes();
    } catch (error) {
        alert('Login failed. Check your credentials.');
    }
}

async function register() {
    const name = document.getElementById('reg-name').value;
    const username = document.getElementById('reg-username').value;
    const password = document.getElementById('reg-password').value;

    try {
        await axios.post(`${API_BASE_URL}/users/register`, { name, username, password });
        alert('Registration successful! Please log in.');
        showLogin();
    } catch (error) {
        alert('Registration failed. Username might already exist.');
    }
}

function logout() {
    authToken = null;
    document.getElementById('main-content').classList.add('hidden');
    document.getElementById('auth-section').classList.remove('hidden');
}

async function loadRentBikes() {
    try {
        const response = await axios.get(`${API_BASE_URL}/bikeRental`);
        const rentBikesContainer = document.getElementById('rent-bikes');
        rentBikesContainer.innerHTML = '';

        response.data.forEach(bike => {
            const bikeCard = document.createElement('div');
            bikeCard.classList.add('bg-white', 'p-4', 'rounded', 'shadow');
            bikeCard.innerHTML = `
                <h3 class="text-xl font-bold">${bike.model}</h3>
                <p>Status: ${bike.available ? 'Available' : 'Rented'}</p>
                <button onclick="rentBike(${bike.id})" 
                    class="mt-2 bg-blue-500 text-white px-4 py-2 rounded"
                    ${!bike.available ? 'disabled' : ''}>
                    Rent Bike
                </button>
            `;
            rentBikesContainer.appendChild(bikeCard);
        });
    } catch (error) {
        console.error('Failed to load bikes', error);
    }
}

async function rentBike(bikeId) {
    try {
        await axios.post(`${API_BASE_URL}/bikeRental/rent/${bikeId}`, null, {
            headers: { 'Authorization': authToken }
        });
        alert('Bike rented successfully!');
        loadRentBikes();
    } catch (error) {
        alert('Failed to rent bike');
    }
}

async function showSaleBikes() {
    document.getElementById('rent-bikes').classList.add('hidden');
    document.getElementById('sale-bikes').classList.remove('hidden');
    document.getElementById('basket').classList.add('hidden');

    try {
        const response = await axios.get(`${API_BASE_URL}/bikeRental/gustaveBike/availableBikes`);
        const saleBikesContainer = document.getElementById('sale-bikes');
        saleBikesContainer.innerHTML = '';

        response.data.forEach(bike => {
            const bikeCard = document.createElement('div');
            bikeCard.classList.add('bg-white', 'p-4', 'rounded', 'shadow');
            bikeCard.innerHTML = `
                <h3 class="text-xl font-bold">${bike.model}</h3>
                <p>Price: €${bike.price}</p>
                <button onclick="addToBasket(${bike.id})" 
                    class="mt-2 bg-green-500 text-white px-4 py-2 rounded">
                    Add to Basket
                </button>
            `;
            saleBikesContainer.appendChild(bikeCard);
        });
    } catch (error) {
        console.error('Failed to load sale bikes', error);
    }
}

async function addToBasket(bikeId) {
    try {
        await axios.post(`${API_BASE_URL}/bikeRental/gustaveBike/basket/${bikeId}`, null, {
            headers: { 'Authorization': authToken }
        });
        alert('Bike added to basket!');
    } catch (error) {
        alert('Failed to add bike to basket');
    }
}

async function showBasket() {
    document.getElementById('rent-bikes').classList.add('hidden');
    document.getElementById('sale-bikes').classList.add('hidden');
    document.getElementById('basket').classList.remove('hidden');

    try {
        const response = await axios.get(`${API_BASE_URL}/bikeRental/gustaveBike/basket`, {
            headers: { 'Authorization': authToken }
        });
        const basketContainer = document.getElementById('basket-items');
        const basketTotalContainer = document.getElementById('basket-total');
        basketContainer.innerHTML = '';

        response.data.bikes.forEach(bike => {
            const bikeCard = document.createElement('div');
            bikeCard.classList.add('bg-white', 'p-4', 'rounded', 'shadow');
            bikeCard.innerHTML = `
                <h3 class="text-xl font-bold">${bike.model}</h3>
                <p>Price: €${bike.price}</p>
            `;
            basketContainer.appendChild(bikeCard);
        });

        basketTotalContainer.innerHTML = `
            <h3 class="text-2xl font-bold">Total: €${response.data.totalPrice}</h3>
        `;
    } catch (error) {
        console.error('Failed to load basket', error);
    }
}

async function buyBasket() {
    try {
        await axios.post(`${API_BASE_URL}/bikeRental/gustaveBike/basket/buy`, null, {
            headers: { 'Authorization': authToken }
        });
        alert('Basket purchased successfully!');
        showBasket(); // Refresh basket view
    } catch (error) {
        alert('Failed to purchase basket');
    }
}

// Initial setup
showLogin();