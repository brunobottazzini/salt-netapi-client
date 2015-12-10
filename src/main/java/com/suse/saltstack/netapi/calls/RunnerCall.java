package com.suse.saltstack.netapi.calls;

import com.google.gson.reflect.TypeToken;
import com.suse.saltstack.netapi.AuthModule;
import com.suse.saltstack.netapi.client.SaltStackClient;
import com.suse.saltstack.netapi.exception.SaltStackException;
import com.suse.saltstack.netapi.results.Result;

import static com.suse.saltstack.netapi.utils.ClientUtils.parameterizedType;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Class representing a function call of a salt runner module.
 *
 * @param <R> the return type of the called function
 */
public class RunnerCall<R> implements Call<R> {

    private final String functionName;
    private final Optional<Map<String, ?>> kwargs;
    private final TypeToken<R> returnType;

    public RunnerCall(String functionName, Optional<Map<String, ?>> kwargs,
            TypeToken<R> returnType) {
        this.functionName = functionName;
        this.kwargs = kwargs;
        this.returnType = returnType;
    }

    public TypeToken<R> getReturnType() {
        return returnType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getPayload() {
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("fun", functionName);
        kwargs.ifPresent(kwargs -> payload.put("kwargs", kwargs));
        return payload;
    }

    /**
     * Calls a runner module function on the master asynchronously and
     * returns information about the scheduled job that can be used to query the result.
     * Authentication is done with the token therefore you have to login prior
     * to using this function.
     *
     * @param client connection instance with Saltstack
     * @return information about the scheduled job
     * @throws SaltStackException if anything goes wrong
     */
    public RunnerAsyncResult<R> callAsync(final SaltStackClient client)
            throws SaltStackException {
        Result<List<RunnerAsyncResult<R>>> wrapper = client.call(
                this, Client.RUNNER_ASYNC, "/",
                new TypeToken<Result<List<RunnerAsyncResult<R>>>>(){});
        RunnerAsyncResult<R> result = wrapper.getResult().get(0);
        result.setType(getReturnType());
        return result;
    }

    /**
     * Calls a runner module function on the master asynchronously and
     * returns information about the scheduled job that can be used to query the result.
     * Authentication is done with the given credentials no session token is created.
     *
     * @param client connection instance with Saltstack
     * @param username username for authentication
     * @param password password for authentication
     * @param authModule authentication module to use
     * @return information about the scheduled job
     * @throws SaltStackException if anything goes wrong
     */
    public RunnerAsyncResult<R> callAsync(final SaltStackClient client, String username,
            String password, AuthModule authModule) throws SaltStackException {
        Map<String, Object> customArgs = new HashMap<>();
        customArgs.putAll(getPayload());
        customArgs.put("username", username);
        customArgs.put("password", password);
        customArgs.put("eauth", authModule.getValue());

        Result<List<RunnerAsyncResult<R>>> wrapper = client.call(
                this, Client.RUNNER_ASYNC, "/run",
                Optional.of(customArgs),
                new TypeToken<Result<List<RunnerAsyncResult<R>>>>(){});
        RunnerAsyncResult<R> result = wrapper.getResult().get(0);
        result.setType(getReturnType());
        return result;
    }

    /**
     * Calls a runner module function on the master and synchronously
     * waits for the result. Authentication is done with the given credentials
     * no session token is created.
     *
     * @param client connection instance with Saltstack
     * @param username username for authentication
     * @param password password for authentication
     * @param authModule authentication module to use
     * @return the result of the called function
     * @throws SaltStackException if anything goes wrong
     */
    public R callSync(final SaltStackClient client, String username, String password,
            AuthModule authModule) throws SaltStackException {
        Map<String, Object> customArgs = new HashMap<>();
        customArgs.putAll(getPayload());
        customArgs.put("username", username);
        customArgs.put("password", password);
        customArgs.put("eauth", authModule.getValue());

        Type listType = parameterizedType(null, List.class, getReturnType().getType());
        Type wrapperType = parameterizedType(null, Result.class, listType);

        @SuppressWarnings("unchecked")
        Result<List<R>> wrapper = client.call(
                this, Client.RUNNER, "/run", Optional.of(customArgs),
                (TypeToken<Result<List<R>>>) TypeToken.get(wrapperType));
        return wrapper.getResult().get(0);
    }

    /**
     * Calls a runner module function on the master and synchronously
     * waits for the result. Authentication is done with the token therefore you
     * have to login prior to using this function.
     *
     * @param client connection instance with Saltstack
     * @return the result of the called function
     * @throws SaltStackException if anything goes wrong
     */
    public R callSync(final SaltStackClient client) throws SaltStackException {
        Type listType = parameterizedType(null, List.class, getReturnType().getType());
        Type wrapperType = parameterizedType(null, Result.class, listType);

        @SuppressWarnings("unchecked")
        Result<List<R>> wrapper = client.call(this, Client.RUNNER, "/",
                (TypeToken<Result<List<R>>>) TypeToken.get(wrapperType));
        return wrapper.getResult().get(0);
    }

}
